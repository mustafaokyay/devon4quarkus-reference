package com.devonfw.quarkus.productmanagement.service.v1;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.springframework.data.domain.Page;
import org.tkit.quarkus.rs.models.PageResultDTO;

import com.devonfw.quarkus.productmanagement.logic.UcFindProduct;
import com.devonfw.quarkus.productmanagement.logic.UcManageProduct;
import com.devonfw.quarkus.productmanagement.service.v1.model.NewProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

//In Quarkus all JAX-RS resources are treated as CDI beans
//default is Singleton scope
@Path("/products")
// how we serialize response
@Produces(MediaType.APPLICATION_JSON)
// how we deserialize params
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestService {

  // using @Context we can inject contextual info from JAXRS(e.g. http request, current uri info, endpoint info...)
  @Context
  UriInfo uriInfo;

  @Inject
  UcFindProduct ucFindProduct;

  @Inject
  UcManageProduct ucManageProduct;

  @GET
  // REST service methods should not declare exceptions, any thrown error will be transformed by exceptionMapper in
  // tkit-rest
  // We did not define custom @Path - so it will use class level path
  public Page<ProductDto> getAll(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProducts(dto);
  }

  @GET
  @Path("criteriaApi")
  public Page<ProductDto> getAllCriteriaApi(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProductsByCriteriaApi(dto);
  }

  @GET
  @Path("queryDsl")
  public Page<ProductDto> getAllQueryDsl(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProductsByQueryDsl(dto);
  }

  @GET
  @Path("query")
  public Page<ProductDto> getAllQuery(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProductsByTitleQuery(dto);
  }

  @GET
  @Path("nativeQuery")
  public Page<ProductDto> getAllNativeQuery(@BeanParam ProductSearchCriteriaDto dto) {

    return this.ucFindProduct.findProductsByTitleNativeQuery(dto);
  }

  @GET
  @Path("ordered")
  public Page<ProductDto> getAllOrderedByTitle() {

    return this.ucFindProduct.findProductsOrderedByTitle();
  }

  @POST
  // We did not define custom @Path - so it will use class level path.
  // Although we now have 2 methods with same path, it is ok, because it is a different method (get vs post)
  public ProductDto createNewProduct(NewProductDto dto) {

    return this.ucManageProduct.saveProduct(dto);
  }

  @GET
  @Path("{id}")
  public ProductDto getProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    return this.ucFindProduct.findProduct(id);
  }

  @GET
  @Path("title/{title}")
  public ProductDto getProductByTitle(@PathParam("title") String title) {

    return this.ucFindProduct.findProductByTitle(title);
  }

  @DELETE
  @Path("{id}")
  public ProductDto deleteProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    return this.ucManageProduct.deleteProduct(id);
  }

  private static class PagedProductResponse extends PageResultDTO<ProductDto> {
  }

}
