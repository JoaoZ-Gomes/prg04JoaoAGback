package br.com.phteam.consultoria.api.config.mapper;

import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ObjectMapperUtil {

    private final ModelMapper modelMapper;

    // O Spring irá injetar o ModelMapper que criarmos no método @Bean abaixo
    public ObjectMapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Mapeia um objeto de Entrada para um objeto de Saída (Output)
     * @param <D> Tipo da Saída (DTO)
     * @param <T> Tipo da Entrada (Model/Entity)
     * @param entity Objeto a ser mapeado (Model/Entity)
     * @param outClass Classe de destino (DTO)
     * @return O objeto DTO mapeado
     */
    public <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    /**
     * Mapeia uma lista (Collection) de objetos de Entrada para uma lista de objetos de Saída.
     * @param <D> Tipo da Saída (DTO)
     * @param <T> Tipo da Entrada (Model/Entity)
     * @param entityList Lista de objetos a ser mapeada
     * @param outClass Classe de destino (DTO)
     * @return Uma lista de objetos DTO mapeados
     */
    public <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outClass) {
        return entityList.stream()
                .map(entity -> map(entity, outClass))
                .collect(Collectors.toList());
    }

    // Configuração do ModelMapper como um Bean para o Spring gerenciar
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}