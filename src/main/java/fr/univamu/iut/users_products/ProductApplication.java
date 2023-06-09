package fr.univamu.iut.users_products;

import fr.univamu.iut.users_products.control.ProductResource;
import fr.univamu.iut.users_products.data.ProductRepositoryMariadb;
import fr.univamu.iut.users_products.service.ProductRepositoryInterface;
import fr.univamu.iut.users_products.service.ProductService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
@ApplicationScoped
public class ProductApplication  extends Application {

    @Override
    public Set<Object> getSingletons() {
        Set<Object> set = new HashSet<>();

        // Creating the database connection and initializing the associated service
        ProductService service = null ;
        try{
            ProductRepositoryMariadb db = new ProductRepositoryMariadb("jdbc:mariadb://mysql-gonzalesl.alwaysdata.net/gonzalesl_cc2", "gonzalesl_cc2", "e9rXXKmTfcQb3kV");
            service = new ProductService(db);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }

        // Creation of the resource by passing it parameter the services to be executed according to the different endpoints proposed (i.e. HTTP requests accepted)
        set.add(new ProductResource(service));

        return set;
    }

    /**
     * Method called by the CDI API to inject the connection to the database at resource creation time
     * @return an object implementing the ProductRepositoryInterface used to access or modify product data
     */
    @Produces
    private ProductRepositoryInterface openDbConnection(){
        ProductRepositoryMariadb db = null;

        try{
            db = new ProductRepositoryMariadb("jdbc:mariadb://mysql-gonzalesl.alwaysdata.net/gonzalesl_cc2", "gonzalesl_cc2", "e9rXXKmTfcQb3kV");
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        return db;
    }

    /**
     * Method to close the database connection when the application is stopped
     * @param productRepo the database connection instantiated in the @openDbConnection method
     */
    private void closeDbConnection(@Disposes ProductRepositoryInterface productRepo ) {
        productRepo.close();
    }
}
