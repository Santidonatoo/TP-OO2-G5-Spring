package oo2.grupo5.turnos.services.implementations;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import oo2.grupo5.turnos.dtos.requests.ServicioRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.services.interfaces.IServicioService;

@Service
public class ServicioServiceImp implements IServicioService {

	private final IServicioRepository exampleRepository;
    private final ModelMapper modelMapper;
    
    public ServicioServiceImp(IServicioRepository exampleRepository, ModelMapper modelMapper) {
        this.exampleRepository = exampleRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public ServicioResponseDTO save(ServicioRequestDTO exampleRequestDTO) {
    	Servicio example = modelMapper.map(exampleRequestDTO, Servicio.class);
    	Servicio saved = exampleRepository.save(example);
        return modelMapper.map(saved, ServicioResponseDTO.class);
    }
}
