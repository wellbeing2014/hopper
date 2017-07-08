package org.zxp.funk.hopper.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.util.ClassUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

public class MainWebInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException
	{
	    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
	    rootContext.scan(ClassUtils.getPackageName(getClass()));
	    rootContext.setServletContext(servletContext);
	    ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
	    dispatcher.setLoadOnStartup(1);
	    dispatcher.addMapping("/");
	    
	    ServletRegistration.Dynamic druidServlet = servletContext.addServlet("druidServlet", new StatViewServlet());
	    druidServlet.addMapping("/druid/*");
	    
	    FilterRegistration.Dynamic druidfilter = servletContext.addFilter("druid",new WebStatFilter());
	    druidfilter.addMappingForServletNames(null, true, "druidServlet");
	    druidfilter.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
	    //解决hibernate 延迟加载的问题
	    FilterRegistration.Dynamic filter = servletContext.addFilter("openSessionInViewFilter", OpenSessionInViewFilter.class);
	    filter.addMappingForServletNames(null, true, "dispatcher");
	    filter.setInitParameter("flushMode","AUTO");
	    servletContext.addListener(new ContextLoaderListener(rootContext));
	}
}
