package cn.smbms.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import java.util.List;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.PageSupport;

@Controller    //让这个类具备处理请求功能
//设置它操作的模块是用户的模块
@RequestMapping("/user")
public class UserContronller {
	//创建一个业务层对象
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	//定义一个可以访问页面的方法
	@RequestMapping("/login.html")
	public String login() {
		//直接访问login.jsp
		return "login";
	}
	//定义一个dologin的请求方法，与数据库中保存的数据信息进行匹配
	//成功进行数据跳转 ，失败在回到登录页面
	@RequestMapping(value = "/dologin.html" ,method = RequestMethod.POST)
	public String dologin(@RequestParam("userCode") String userCode,
			@RequestParam("userPassword") String userPassword,
			HttpServletRequest requset,HttpSession session) throws Exception{
		//调用业务层方法	
		User user=userService.login(userCode, userPassword);
		if (user!=null) {
			//跳转首页    书写相当于转发
			//如果要使用重定向来进行跳转
			//可以使用redirect来标识
			//forward:逻辑视图名，
			//这两标识后面跟的是一个请求的URL
			//
			//return "forward:frame.html";
			session.setAttribute("loginuser", user);
			return "redirect:frame.html";
		}else {
			//这种是服务器端的跳转（转发）
			/*requset.setAttribute("error","用户名或者密码错误");
			return "login";*/
			throw new Exception("用户名或密码错误!!!!");
		}
	}
	//定义一个实现转发的请求方法
	@RequestMapping("/frame.html")
	public String main() {
		return "frame";
	}
	
	/*//异常处理
	@RequestMapping(value = "exlogin.html",method = RequestMethod.GET)
	public String exlogin(@RequestParam("userCode") String userCode,
			@RequestParam("userPassword") String userPassword) {
		User user=userService.login(userCode, userPassword);
		if (user==null) {
			//抛出一个异常
			throw new RuntimeException("用户名或密码错误 exlogin");
		}
		 return "redirect:frame.html";
	}*/
	//有全局异常就注释局部异常
	//处理异常登录的操作    注解支持HandelrExceptionResolver视图的一种方式 
				//HandelrExceptionResolver视图中有一个resolveHandler方法，
				//如果有异常就处理，无异常就不做处理
	/*@ExceptionHandler(value = RuntimeException.class)
	public String handleException(RuntimeException e,
			HttpServletRequest req) {
		req.setAttribute("excption",e);
		return "error";
	}*/
	
	//退出                                                                               不能用post，报错
	 //									    a标签的默认提交方式是get
	@RequestMapping(value = "/logout.html" ,method = RequestMethod.GET)
	public String logout(HttpSession session,HttpServletRequest req) {
		if (session!=null) {
			//req.getSession().invalidate();
			session.invalidate();
		}
		return "redirect:login.html";
		//return "redirect:/login.html";
	}
	/*@RequestMapping("/logout")
	public String logout(HttpSession session) {
		if (session!=null) {
			session.invalidate();
		}
		return "login";
	}*/
	
	//查询用户列表的方法
	@RequestMapping(value="/userlist.html",method=RequestMethod.GET)
    public String query(HttpServletRequest request, 
        HttpServletResponse response,
        @RequestParam(name="queryname",required = false) String queryname,
        @RequestParam(name="queryUserRole",required = false) String queryUserRole,
        @RequestParam(name="pageIndex",required = false) String pageIndex,
        Model model){
        //查询用户列表   保存用户id
        int _queryUserRole = 0;
        List<User> userList = null;
        //设置页面容量
        int pageSize = 5;
        //当前页码
        int currentPageNo = 1;
        if(queryname == null){
            queryname = "";
        }
        //判断角色id是否有数据
        if(queryUserRole != null && !queryUserRole.equals("")){
            _queryUserRole = Integer.parseInt(queryUserRole);
        }
        //判断页面的值
        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
            	//使用全局异常来进行异常信息的显示
            	throw new RuntimeException("页面的值不正确");
                //response.sendRedirect("error.jsp");
            }
        }   
        //总数量（表）    
        int totalCount  = userService.getUserCount(queryname,_queryUserRole);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        
        int totalPageCount = pages.getTotalPageCount();
        
        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        
        //获得显示数据的集合
        userList = userService.getUserList(queryname,_queryUserRole,currentPageNo, pageSize);
        //讲数据放到model对象中
        model.addAttribute("userList",userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryname);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        //request.getRequestDispatcher("userlist.jsp").forward(request, response);
        return "userlist";
    }
	
	//添加用户的跳转到adduser.jsp
	@RequestMapping(value = "/useradd.html" ,method = RequestMethod.GET)
	//1、将创建的对象保存到数据模型中
	//public String addUser(User user,Model model) {
	//2、使用注解将对象保存
	public String addUser(@ModelAttribute("user") User user) {
		//此处的User就相当于创建一个保存用户信息的空的对象
		//在表单中输入数据后，数据就会自动的装到user对象中
		//1、model.addAttribute("user",user);
		//2、
		return "useradd";
	}
	
	//增加用户
	@RequestMapping(value = "/usersave.html",method = RequestMethod.POST)
	public String saveUser( User user,HttpSession session,
			HttpServletRequest req,
			@RequestParam(value  = "a_idPicPath",required = false) MultipartFile attch) {
		//上传文件的名称
		String idPicPath=null;
		String path=session.getServletContext().getRealPath("static"+File.separator+"upload" );
		//判断文件是否为空
		if (!attch.isEmpty()) {
			//上传文件的名称
			String oldFileName=attch.getOriginalFilename();
			System.out.println("上传文件名字："+oldFileName);
			
			//获得上传文件的后缀名
			String suffix=FilenameUtils.getExtension(oldFileName);
			System.out.println("上传文件的后缀名："+suffix);
			
			//设置文件上传的大小
			int fileSize=512000;
			
			//要求上传文件的后缀名只能是jpg，png，gif
			//判断文件大小是否超过了设置的大小，超过了就响应一个错误信息
			if (attch.getSize()>fileSize) {
				req.setAttribute("error","上传文件大小不能大于1000k");
				//重新回到添加页面
				return "useradd";
			}else if (suffix.equalsIgnoreCase("jpg") 
				   || suffix.equalsIgnoreCase("png")
				   || suffix.equalsIgnoreCase("gif")) {
				//对上传的文件进行一个设置时间戳+随机数+oldFileName
				String fileName=
						System.currentTimeMillis()+RandomUtils.nextInt(0,1000000)+oldFileName;
				File tagfile=new File(path,fileName);
				//判断上传文件是否存在，如果不存在就创建
				if (!tagfile.exists()) {
					//创建文件以及文件夹
					tagfile.mkdirs();
				}
				
				try {
					//进行文件的上传
					attch.transferTo(tagfile);
				}catch (Exception e) {
					req.setAttribute("error","上传失败！！！" );
					return "useradd";
				}
				
				//为保存到数据库中的变量赋值
				idPicPath=path+File.separator+fileName;
			}else {
				req.setAttribute("error", "图片格式不正确！！！");
				return "useradd";
			}
		}
		
		//需要创建者的id
		int createdBy=((User)session.getAttribute("loginuser")).getId();
		//获取当前系统时间
		user.setCreatedBy(createdBy);
		user.setCreationDate(new Date());
		user.setIdPicPath(idPicPath);
		//调用增加用户的方法
		boolean isOk=userService.add(user);
		
		if (isOk) {
			//成功过后，用户列表页面需要更新数据，刷新
			return "redirect:userlist.html";
		}else {
			return "useradd";
		}
	}
	
	//创建一个修改页面的请求方法
	@RequestMapping(value = "/usermodify.html",method = RequestMethod.GET)
	/**
	 * 
	 * @param uid 需要根据id进行查找的实体主键
	 * @param model 保存根据id查找的用户实体
	 * @return  修改页面的逻辑视图名
	 */
	public String usermodify(@RequestParam("uid") String uid,Model model) {
		//调用根据id查找用户的方法
		User user =userService.getUserById(uid);
		model.addAttribute("user",user);
		return "usermodify";
	}
	@RequestMapping(value = "/modifysave.html",method = RequestMethod.POST)
	/**
	 * 实现用户修改的方法
	 * @param user 需要修改的对象
	 * @param session 用于获得保存在Session中的对象
	 * @return
	 */
	public String name(User user,HttpSession session) {
		//获得当前登录的用户id
		int userId=((User) session.getAttribute("loginuser")).getId();
		//为用户对象
		user.setModifyBy(userId);
		user.setModifyDate(new Date());
		//调用修改方法
		boolean isOk=userService.modify(user);
		if (isOk) {
			//重新请求查询用户列表
			return "redirect:userlist.html";
		}
		return "usermodify";
	}
	
	//Spring的标签提交
	@RequestMapping(value = "/add.html",method = RequestMethod.GET)
	public String add(@ModelAttribute("user") User user) {
		return "user/user-add";
		
	}
	//增加也使用同一个请求
	//需要书爷JSR303的数据效验的框架，需要在实体类前面加上一个@Valid的注解
	//在这个参数后面紧跟着一个参数BindingResult,该参数的作用就是去获取验证的结果
	@RequestMapping(value = "/add.html",method = RequestMethod.POST)
	public String addsave(@Valid User user,BindingResult result,
			HttpSession session) {
		//在执行添加之前，先判断获得验证的结果是否有异常
		//hasErrors（）方法去判断是否有异常
		if (result.hasErrors()) {
			return "user/user-add";
		}
		//需要创建者的id
				int createdBy=((User)session.getAttribute("loginuser")).getId();
				//获取当前系统时间
				user.setCreatedBy(createdBy);
				user.setCreationDate(new Date());
				//调用增加用户的方法
				boolean isOk=userService.add(user);
				
				if (isOk) {
					//成功过后，用户列表页面需要更新数据，刷新
					return "redirect:userlist.html";
				}else {
					return "user/user-add";
				}
	}
	
	//使用REST风格访问路径
	@RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
	public String view(@PathVariable String id,Model model) {
		//调用根据id查找查找实体的方法
		User user =userService.getUserById(id);
		model.addAttribute("user",user);
		return "userview";
	}
	
	
	
	
	
	
	
	
	
	//用户角色的显示
	@RequestMapping(value = "/rolelist.html",method = RequestMethod.GET)
	public String rolelist(Model model) {
		List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
		return "rolelist";
	}
	
	//用户角色
	@RequestMapping(value = "/userNadd.html",method = RequestMethod.GET)
	public String Roleadd(@ModelAttribute("Role") Role role) {
		return "RoleAdd";
	}
	
	@RequestMapping(value = "/rolesave.html",method = RequestMethod.POST)
	public String roleAdd(Role role,HttpSession session) {
		int createdBy=((User)session.getAttribute("loginuser")).getId();
		role.setCreatedBy(createdBy);
		role.setCreationDate(new Date());
		boolean isOk=roleService.add(role);
		if (isOk) {
			//成功过后，用户列表页面需要更新数据，刷新
			return "redirect:rolelist.html";
		}else {
			return "RoleAdd";
		}
	}
	
	
	
}
