package org.zxp.funk.hopper.jpacontroller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zxp.funk.hopper.entity.User;
import org.zxp.funk.hopper.service.UserService;

@Controller
@RequestMapping({"user"})
public class UserController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    UserService userService;
	
	
	@RequestMapping({"login"})
	public String loginPage()
	{
	    return "user/login";
	}
	
	@RequestMapping({"{username}"})
	  public String home(@PathVariable("username") String username)
	  {
	    return "doc/list-user";
	  }
	  
	  @RequestMapping({"{username}/{label}"})
	  public String label(@PathVariable("username") String username, @PathVariable("label") String label)
	  {
	    return "doc/list-user";
	  }
	
	@ResponseBody
	  @RequestMapping({"checkLogin.json"})
	  public Map<String, String> checkLogin(HttpServletRequest req) {
	    Map<String, String> map = new HashMap<String, String>();
	    try {
	      
	      User vo = this.userService.findUser("qweqw");
	      logger.debug("get user vo: " + vo);
	      if (null != vo) {
	        map.put("uid", vo.getId());
	        map.put("sid", vo.getId());
	        map.put("username", vo.getName());
	        
	        return map;
	      }
	      map.put("error", "未登录！");
	      return map;
	    }
	    catch (Exception e) {
	      logger.error("checkLogin fail: " + e.getMessage());
	      map.put("error", e.getMessage()); }
	    return map;
	  }
	
}

