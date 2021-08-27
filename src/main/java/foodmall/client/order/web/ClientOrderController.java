package foodmall.client.order.web;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import admin.user.com.service.MberManageVO;
import admin.user.com.service.UserDefaultVO;
import admin.user.com.service.UserManageVO;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.rte.fdl.property.EgovPropertyService;
import foodmall.client.order.service.CartListVO;
import foodmall.client.order.service.ClientOrderService;
import foodmall.client.order.service.OrderDetailVO;
import foodmall.client.order.service.OrderVO;

@Controller
public class ClientOrderController {

	@Resource(name = "clientOrderService")
    private ClientOrderService clientOrderService;
	
	/** cmmUseService */
	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** DefaultBeanValidator beanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	// 장바구니 담기
		@ResponseBody
		@RequestMapping(value = "/view/addCart", method = RequestMethod.POST)
		public int addCart(CartListVO cart, HttpSession session) throws Exception {
			
			int result = 0;
			
			MberManageVO member = (MberManageVO)session.getAttribute("member");
			
			if(member != null) {
				cart.setEmailId(member.getEmailId());
				clientOrderService.addCart(cart);
				result = 1;
			}
			
			return result;
		}
	//장바구니 목록
	@RequestMapping("/order/ClientCartView.do")
	public String CartView(HttpSession session, @ModelAttribute("orderVO") OrderVO orderVO, @ModelAttribute("cartListVO") CartListVO cartListVO, Model model)
			throws Exception {
		MberManageVO member = (MberManageVO)session.getAttribute("member");
		String emailId = member.getEmailId();
	    System.out.println(member.getEmailId());
		List<CartListVO> cartList = clientOrderService.cartList(emailId);
		  
		  model.addAttribute("cartList", cartList);
			return "food/shopingCart";
	
	}
	/*
	// 장바구니 삭제
		@ResponseBody
		@RequestMapping(value = "/deleteCart", method = RequestMethod.POST)
		public int deleteCart(HttpSession session, @RequestParam(value = "chbox[]") List<String> chArr, CartVO cart) throws Exception {
			logger.info("delete cart");
			
			MemberVO member = (MemberVO)session.getAttribute("member");
			String userId = member.getUserId();
			
			int result = 0;
			int cartNum = 0;		
			
			// 로그인 여부 구분
			if(member != null) {
				cart.setUserId(userId);
				
				for(String i : chArr) {  // 에이젝스에서 받은 chArr의 갯수만큼 반복
					cartNum = Integer.parseInt(i);  // i번째 데이터를 cartNum에 저장
					cart.setCartNum(cartNum);
					service.deleteCart(cart);
				}			
				result = 1;
			}		
			return result;		
		}*/
	//주문
	@RequestMapping("/order/clientCheckOut")
	 public String order(HttpSession session, @ModelAttribute("orderVO") OrderVO orderVO, OrderDetailVO orderDetailVO,
			 @RequestParam(value = "chk[]") List<String> chArr) throws Exception {
	 // Logger.info("order");
	  
	  String emailId = (String)session.getAttribute("member");  
	  
    /* //주문번호(orderId) 생성을 위한 로직
	  Calendar cal = Calendar.getInstance();
	  int year = cal.get(Calendar.YEAR);
	  String ym = year + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
	  String ymd = ym +  new DecimalFormat("00").format(cal.get(Calendar.DATE));
	  String subNum = "";
	  
	  for(int i = 1; i <= 6; i ++) {
	   subNum += (int)(Math.random() * 10);
	  }
	  
	  String orderSeq = ymd + "_" + subNum; //ex) 20200508_373063
	  orderVO.setOrderSeq(orderSeq);
	  orderVO.setEmailId(emailId);*/
	  
	  clientOrderService.orderInsert(orderVO); 
	  clientOrderService.orderInfoDetails(orderDetailVO); //주문 상세 테이블 insert
	 // clientOrderService.cartAllDelete(emailId);
	  /*int cartNum = 0;
	  for(String i : chArr){
		  cartNum = Integer.parseInt(i);
		  System.out.println("cart -> CHK orderList : "+cartNum);
		  System.out.println("cart -> orderId orderList : "+orderSeq);
		   clientOrderService.orderInfoDetails(orderSeq,cartNum);
		 // clientOrderService.cartDelete(cartNum); //체크되어 들어온 cart번호로 cart table delete
	  }*/
	  
	  
	  return "redirect:/food/myPage";  
	 }
	
	/*// 주문 목록
		@RequestMapping(value = "/orderList", method = RequestMethod.GET)
		public void getOrderList(HttpSession session, OrderVO order, Model model) throws Exception {
			logger.info("get order list");
			
			MemberVO member = (MemberVO)session.getAttribute("member");
			String userId = member.getUserId();
			
			order.setUserId(userId);
			
			List<OrderVO> orderList = service.orderList(order);
			
			model.addAttribute("orderList", orderList);
		}
		 
		// 주문 상세 목록
		@RequestMapping(value = "/orderView", method = RequestMethod.GET)
		public void getOrderList(HttpSession session,
								@RequestParam("n") String orderId,
								OrderVO order, Model model) throws Exception {
			logger.info("get order view");
			
			MemberVO member = (MemberVO)session.getAttribute("member");
			String userId = member.getUserId();
			
			order.setUserId(userId);
			order.setOrderId(orderId);
			
			List<OrderListVO> orderView = service.orderView(order);
			
			model.addAttribute("orderView", orderView);
		}
		*/
		
}
