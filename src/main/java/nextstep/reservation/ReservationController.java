package nextstep.reservation;

import nextstep.auth.AuthenticatedUsername;
import nextstep.exceptions.exception.ReservationForbiddenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity createReservation(
            @RequestBody ReservationRequest reservationRequest,
            @AuthenticatedUsername String username
    ) {
        Long id = reservationService.create(reservationRequest);
        if (!reservationRequest.getName().equals(username)) {
            throw new ReservationForbiddenException("예약자가 일치해야만 예약을 생성할 수 있습니다.");
        }
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(
            @PathVariable Long id,
            @AuthenticatedUsername String username
    ) {
        Reservation reservation = reservationService.findById(id);
        if (!reservation.getName().equals(username)) {
            throw new ReservationForbiddenException("예약당사자만 예약을 삭제할 수 있습니다.");
        }
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}