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

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;

@Controller
@RequestMapping("/provider")
public class Providercontroller {
	@Autowired
	private ProviderService providerService;
	
	//查询供应商
	@RequestMapping(value = "/providerList.html" ,method = RequestMethod.GET)
	public String query(@RequestParam(name = "queryProName",required = false) String queryProName,
					@RequestParam(name = "queryProCode",required = false) String queryProCode,
					Model model)
					throws ServletException, IOException {
		if(StringUtils.isNullOrEmpty(queryProName)){
			queryProName = "";
		}
		if(StringUtils.isNullOrEmpty(queryProCode)){
			queryProCode = "";
		}
		List<Provider> providerList = new ArrayList<Provider>();
		providerList = providerService.getProviderList(queryProName,queryProCode);
		model.addAttribute("providerList", providerList);
		model.addAttribute("queryProName", queryProName);
		model.addAttribute("queryProCode", queryProCode);
		
		return "providerlist";
	}
	
	@RequestMapping(value = "/provideradd.html",method = RequestMethod.GET)
	public String ProviderAdd(@RequestParam(name = "provider",required = false) Provider provider) {
		return "provideradd";
	}
	
	@RequestMapping(value = "/providersave.html" ,method = RequestMethod.POST)
	public String ProviderSave(Provider provider,HttpSession session) {
		int createdBy=((User)session.getAttribute("user")).getId();
		provider.setCreatedBy(createdBy);
		provider.setCreationDate(new Date());
		boolean isOk=providerService.add(provider);
		if (isOk) {
			//成功过后，用户列表页面需要更新数据，刷新
			return "redirect:providerList.html";
		}else {
			return "provideradd";
		}
	}
}
