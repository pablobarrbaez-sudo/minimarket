package com.jpbb.minimarket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.jpbb.minimarket.model.Producto;
import com.jpbb.minimarket.model.Usuario;
import com.jpbb.minimarket.repository.ProductoRepository;
import com.jpbb.minimarket.repository.UsuarioRepository;

@SpringBootApplication
public class MinimarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinimarketApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductoRepository pr, UsuarioRepository ur) {
		return args -> {
			if (pr.count() == 0) {
				pr.saveAll(java.util.List.of(
						crear("Leche 1L", 28.0), crear("Pan Bimbo", 35.0), crear("Huevos 12 pzs", 45.0),
						crear("Coca-Cola 600ml", 18.0), crear("Arroz 1kg", 25.0), crear("Frijol 1kg", 30.0),
						crear("Aceite 1L", 42.0), crear("Jabón Zote", 15.0), crear("Papel Higiénico", 22.0),
						crear("Sabritas", 20.0)
				));
			}
			if (ur.findByUsername("admin").isEmpty()) {
				Usuario a = new Usuario();
				a.setNombre("Admin"); a.setUsuario("admin"); a.setUsername("admin");
				a.setPassword("123"); a.setRol("ADMIN"); ur.save(a);
			}
		};
	}

	private static Producto crear(String n, Double p) {
		Producto pr = new Producto(); pr.setNombre(n); pr.setPrecio(p); pr.setStock(20); return pr;
	}
}