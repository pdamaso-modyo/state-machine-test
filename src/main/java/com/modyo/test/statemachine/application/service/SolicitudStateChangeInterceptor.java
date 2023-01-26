package com.modyo.test.statemachine.application.service;

import static com.modyo.test.statemachine.config.statemachine.StateMachineConfig.SM_ENTITY_HEADER;

import com.modyo.test.statemachine.application.port.out.LoadSolicitudPort;
import com.modyo.test.statemachine.application.port.out.SaveSolicitudPort;
import com.modyo.test.statemachine.config.statemachine.EventsEnum;
import com.modyo.test.statemachine.config.statemachine.StatesEnum;
import com.modyo.test.statemachine.domain.model.Solicitud;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class SolicitudStateChangeInterceptor extends StateMachineInterceptorAdapter<StatesEnum, EventsEnum> {

  private final LoadSolicitudPort loadPort;
  private final SaveSolicitudPort savePort;

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void preStateChange(State<StatesEnum, EventsEnum> state, Message<EventsEnum> message,
      Transition<StatesEnum, EventsEnum> transition, StateMachine<StatesEnum, EventsEnum> stateMachine,
      StateMachine<StatesEnum, EventsEnum> rootStateMachine) {
    log.info("Save state change");
    Optional.ofNullable(message)
        .ifPresent(msg -> Optional.ofNullable((Long) (msg.getHeaders().getOrDefault(SM_ENTITY_HEADER, -1L)))
            .ifPresent(solicitudId -> {
              Solicitud solicitud = loadPort.loadAndLock(solicitudId);
              solicitud.setState(state.getId().name());
              savePort.save(solicitud);
              log.info("State change saved for {} {}", solicitudId, state.getId().name());
            }));
  }
}
