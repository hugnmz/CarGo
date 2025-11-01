package listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.List;

import service.LocationService;
import service.impl.LocationServiceImpl;
import dto.LocationDTO;
import util.di.DIContainer;

@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        LocationService locationService = DIContainer.get(LocationService.class);
        List<LocationDTO> locations = locationService.getAllLocations();  

        ctx.setAttribute("locations", locations);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
