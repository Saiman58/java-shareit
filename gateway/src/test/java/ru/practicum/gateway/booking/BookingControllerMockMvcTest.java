package ru.practicum.gateway.booking;

/*import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.dto.booking.BookingResponseDto;
import ru.practicum.dto.booking.BookingState;
import ru.practicum.dto.item.ItemDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private BookingResponseDto bookingResponseDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        bookingResponseDto = BookingResponseDto.builder()
                .item(itemDto)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    void bookItem_returnsOk() throws Exception {
        when(bookingClient.getBookingById(eq(1L), any()))
                .thenReturn(ResponseEntity.ok(Collections.singletonMap("result", "success")));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingResponseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));
    }

    @Test
    void getBooking_returnsOk() throws Exception {
        when(bookingClient.getBookingById(1L, 1L))
                .thenReturn(ResponseEntity.ok(Collections.singletonMap("bookingId", 1)));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1));
    }

    @Test
    void getBookings_withValidState_returnsOk() throws Exception {
        when(bookingClient.getUserBookings(1L, BookingState.ALL, 0, 10))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_withInvalidState_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "INVALID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookings_withFromSize_returnsOk() throws Exception {
        when(bookingClient.getBookings(1L, BookingState.ALL, 5, 20))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "all")
                        .param("from", "5")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_whenInvalidUserId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approveBooking_returnsOk() throws Exception {
        when(bookingClient.approveBooking(1L, 1L, true))
                .thenReturn(ResponseEntity.ok(Collections.singletonMap("approved", true)));

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true));
    }

    @Test
    void getBookingsForOwner_returnsOk() throws Exception {
        when(bookingClient.getBookingsForOwner(1L, BookingState.ALL, 0, 10))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }
}*/