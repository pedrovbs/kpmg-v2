package com.upoiny.kpmgv2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kpmgOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KPMG Fusion Simulator API")
                        .version("v1")
                        .description("API didática para integração com Oracle Visual Builder Studio. "
                                + "As listagens suportam paginação, ordenação e busca pelo parâmetro search.")
                        .license(new License().name("Uso educacional")))
                .addTagsItem(new Tag().name("HCM e CX").description("Funcionários, cargos, departamentos e clientes."))
                .addTagsItem(new Tag().name("SCM").description("Produtos, categorias, fornecedores e estoque."))
                .addTagsItem(new Tag().name("ERP").description("Compras, vendas e seus itens."));
    }

    @Bean
    public GroupedOpenApi hcmCxApi() {
        return GroupedOpenApi.builder()
                .group("hcm-cx")
                .pathsToMatch("/api/clientes/**", "/api/funcionarios/**", "/api/cargos/**", "/api/departamentos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi scmApi() {
        return GroupedOpenApi.builder()
                .group("scm")
                .pathsToMatch("/api/produtos/**", "/api/categorias/**", "/api/fornecedores/**")
                .build();
    }

    @Bean
    public GroupedOpenApi erpApi() {
        return GroupedOpenApi.builder()
                .group("erp")
                .pathsToMatch("/api/compras/**", "/api/itens-compra/**", "/api/vendas/**", "/api/itens-venda/**")
                .build();
    }
}
