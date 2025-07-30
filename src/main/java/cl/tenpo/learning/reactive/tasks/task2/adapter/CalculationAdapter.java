package cl.tenpo.learning.reactive.tasks.task2.adapter;

import cl.tenpo.learning.reactive.tasks.task2.domain.model.Calculation;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.request.CalculationRequest;
import cl.tenpo.learning.reactive.tasks.task2.infraestructure.dto.inbound.response.CalculationResponse;
import org.springframework.stereotype.Component;

@Component
public class CalculationAdapter {
    public Calculation toModel(CalculationRequest calculationRequest){
    return Calculation.builder()
        .number1(calculationRequest.number1())
        .number2(calculationRequest.number1())
        .build();
    }

    public CalculationResponse toResponse(Double result){
        return CalculationResponse.builder()
                .result(result)
                .build();
    }
}
