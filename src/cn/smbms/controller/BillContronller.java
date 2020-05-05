package cn.smbms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;

@Controller
@RequestMapping("/bill")
public class BillContronller {
	@Autowired
	private BillService billService;
	@Autowired
	private ProviderService providerService;
	//查询订单
	@RequestMapping(value = "/billList.html" , method = RequestMethod.GET )
	public String query(@RequestParam(name = "queryProductName",required = false ) String queryProductName,
						 @RequestParam(name = "queryProviderId",required = false) String queryProviderId,
						 @RequestParam(name = "queryIsPayment" ,required = false) String queryIsPayment,
						 Model model,HttpSession session)throws ServletException, IOException {
		List<Provider> providerList = new ArrayList<Provider>();
		providerList = providerService.getProviderList("","");
		session.setAttribute("providerList",providerList);
		if(StringUtils.isNullOrEmpty(queryProductName)){
			queryProductName = "";
		}
		
		List<Bill> billList = new ArrayList<Bill>();
		Bill bill = new Bill();
		if(StringUtils.isNullOrEmpty(queryIsPayment)){
			bill.setIsPayment(0);
		}else{
			bill.setIsPayment(Integer.parseInt(queryIsPayment));
		}
		
		if(StringUtils.isNullOrEmpty(queryProviderId)){
			bill.setProviderId(0);
		}else{
			bill.setProviderId(Integer.parseInt(queryProviderId));
		}
		bill.setProductName(queryProductName);
		billList = billService.getBillList(bill);
		model.addAttribute("billList", billList);
		model.addAttribute("queryProductName", queryProductName);
		model.addAttribute("queryProviderId", queryProviderId);
		model.addAttribute("queryIsPayment", queryIsPayment);
		
		return "billlist";
	}
	
	
	//添加订单
	//1、跳转到添加页面
	@RequestMapping(value = "/billadd.html",method = RequestMethod.GET)
	public String Billadd(@RequestParam(name = "bill",required = false)  Bill bill,Model model) {
		return "billadd";
	}
	
	//2、添加页面的执行
	@RequestMapping(value = "/billsave.html",method = RequestMethod.GET)
	public String BillSave(Bill bill,HttpSession session,HttpServletRequest req) {
		//商品描述，供应商id，创建者，创建时间
		String productDesc=((Bill)session.getAttribute("bill")).getProviderName();
		bill.setProductDesc(productDesc);
		int createdBy=((User)session.getAttribute("user")).getId();
		bill.setCreatedBy(createdBy);
		bill.setCreationDate(new Date());
		boolean isOk=billService.add(bill);
		if (isOk) {
			return "redirect:billlist.html";
		}else {
			return "billadd.html";
		}
		
	}
	
	
	
}
