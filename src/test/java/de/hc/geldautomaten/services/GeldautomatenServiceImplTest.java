package de.hc.geldautomaten.services;

import de.hc.geldautomaten.entities.Geldautomat;
import de.hc.geldautomaten.entities.GeldautomatImpl;
import de.hc.geldautomaten.records.Location;
import de.hc.geldautomaten.repositories.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GeldautomatenServiceImplTest {

    private GeldautomatenService geldautomatenService;

    @Mock
    private Repository mockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        geldautomatenService = new GeldautomatenServiceImpl(mockRepository);
    }

    @Test
    void create_sollteRepositoryAufrufenUndNummerZurueckgeben() {
        Location location = new Location(50.0, 7.0);
        BigDecimal bargeld = new BigDecimal("30000.00");
        Geldautomat dummyAutomat = new GeldautomatImpl(123L, location, bargeld);
        when(mockRepository.createGeldautomat(location, bargeld)).thenReturn(dummyAutomat);

        long erstellteNummer = geldautomatenService.create(location, bargeld);

        assertThat(erstellteNummer).isEqualTo(123L);
        InOrder inOrder = Mockito.inOrder(mockRepository);
        inOrder.verify(mockRepository).beginTransaction();
        inOrder.verify(mockRepository).createGeldautomat(location, bargeld);
        inOrder.verify(mockRepository).commitTransaction();
        Mockito.verify(mockRepository, Mockito.never()).rollbackTransaction();
    }

    @Test
    void create_sollteBeiRepositoryFehlerRollbackAusfuehren() {
        Location location = new Location(50.0, 7.0);
        BigDecimal bargeld = new BigDecimal("30000.00");

        when(mockRepository.createGeldautomat(location,bargeld))
                .thenThrow(new RuntimeException("DB Fehler"));

        assertThatThrownBy(() -> {
            geldautomatenService.create(location, bargeld);
        })
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Geldautomat konnte nicht erstellt werden");

        InOrder inOrder = Mockito.inOrder(mockRepository);
        inOrder.verify(mockRepository).beginTransaction();
        inOrder.verify(mockRepository).rollbackTransaction();
        Mockito.verify(mockRepository, Mockito.never()).commitTransaction();
    }
}

