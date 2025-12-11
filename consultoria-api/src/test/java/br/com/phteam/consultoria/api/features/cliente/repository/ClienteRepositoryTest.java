package br.com.phteam.consultoria.api.features.cliente.repository;

import br.com.phteam.consultoria.api.features.cliente.model.Cliente;
import br.com.phteam.consultoria.api.features.consultor.model.Consultor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EntityManager em;

    private Consultor criarConsultorValido() {
        Consultor c = new Consultor();
        c.setNome("Consultor Teste");
        c.setEmail("consultor@test.com");
        c.setSenha("123456");        // herdado de Usuario
        c.setTelefone("74999999999");
        c.setNumeroCref("ABC123");
        c.setEspecializacao("Treinamento");
        return c;
    }

    private Cliente criarClienteValido(String email, Consultor consultor, String objetivo) {
        Cliente c = new Cliente();
        c.setNome("João Teste");
        c.setEmail(email);
        c.setSenha("123"); // herdado de Usuario
        c.setConsultor(consultor);
        c.setObjetivo(objetivo);
        return c;
    }

    @Test
    @DisplayName("Deve salvar e buscar cliente por email")
    void testFindByEmail() {
        Consultor consultor = criarConsultorValido();
        em.persist(consultor);

        Cliente cliente = criarClienteValido("teste@email.com", consultor, "Hipertrofia");
        clienteRepository.save(cliente);

        Optional<Cliente> encontrado = clienteRepository.findByEmail("teste@email.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("teste@email.com");
    }

    @Test
    @DisplayName("Deve buscar clientes por consultor_id")
    void testFindByConsultorId() {

        Consultor consultor10 = criarConsultorValido();
        Consultor consultor20 = criarConsultorValido();
        consultor20.setEmail("outro@test.com");

        em.persist(consultor10);
        em.persist(consultor20);

        clienteRepository.save(criarClienteValido("a@mail.com", consultor10, "Hipertrofia"));
        clienteRepository.save(criarClienteValido("b@mail.com", consultor10, "Cardio"));
        clienteRepository.save(criarClienteValido("c@mail.com", consultor20, "Força"));

        List<Cliente> lista = clienteRepository.findByConsultorId(consultor10.getId());

        assertThat(lista).hasSize(2);
        assertThat(lista)
                .extracting(Cliente::getEmail)
                .containsExactlyInAnyOrder("a@mail.com", "b@mail.com");
    }

    @Test
    @DisplayName("Deve buscar clientes pelo objetivo")
    void testFindByObjetivo() {

        Consultor consultor = criarConsultorValido();
        em.persist(consultor);

        clienteRepository.save(criarClienteValido("x@mail.com", consultor, "Hipertrofia"));
        clienteRepository.save(criarClienteValido("y@mail.com", consultor, "Hipertrofia"));
        clienteRepository.save(criarClienteValido("z@mail.com", consultor, "Emagrecimento"));

        List<Cliente> lista = clienteRepository.findByObjetivo("Hipertrofia");

        assertThat(lista).hasSize(2);
    }
}
