package top.naccl.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.annotation.AccessLimit;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.User;
import top.naccl.model.dto.LoginInfo;
import top.naccl.model.vo.Result;
import top.naccl.service.RedisService;
import top.naccl.service.UserService;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.JwtUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 前台登录
 * @Author: Naccl
 * @Date: 2020-09-02
 */
@RestController
public class LoginController {
	@Autowired
	UserService userService;
	@Resource
	private RedisService redisService;
	/**
	 * 登录成功后，签发博主身份Token
	 *
	 * @param loginInfo
	 * @return
	 */
	@PostMapping("/login")
	public Result login(@RequestBody LoginInfo loginInfo) {
		User user = userService.findUserByUsernameAndPassword(loginInfo.getUsername(), loginInfo.getPassword());
		if (!"ROLE_admin".equals(user.getRole())) {
			return Result.create(403, "无权限");
		}
		user.setPassword(null);
		String jwt = JwtUtils.generateToken(JwtConstants.ADMIN_PREFIX + user.getUsername());
		Map<String, Object> map = new HashMap<>(4);
		map.put("user", user);
		map.put("token", jwt);
		return Result.ok("登录成功", map);
	}

	/**
	 * 访问网站需要验证
	 * @param loginInfo
	 * @param request
	 * @return
	 */
	@AccessLimit(seconds = 60,maxCount = 3)
	@PostMapping("/check")
	public Result check(@RequestBody LoginInfo loginInfo,HttpServletRequest request) {
		String checkCode = loginInfo.getPassword();
		String value = redisService.getStringValue(checkCode);
		String ipAddress = IpAddressUtils.getIpAddress(request);
		if (StringUtils.isEmpty(value)){
			//验证失败
			return Result.create(401, "验证失败");
		}else {
			//验证成功清除限制
			String method = request.getMethod();
			String requestURI = request.getRequestURI();
			String redisKey = ipAddress + ":" + method + ":" + requestURI;
			redisService.deleteCacheByKey(redisKey);
		}
		String jwt = JwtUtils.generateToken(JwtConstants.ADMIN_PREFIX + ipAddress);
		Map<String, Object> map = new HashMap<>(2);
		map.put("token", jwt);
		return Result.ok("验证成功", map);
	}
}
