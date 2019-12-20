package edu.uclm.esi.iso2.banco20193capas;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uclm.esi.iso2.banco20193capas.model.Cuenta;
import edu.uclm.esi.iso2.banco20193capas.model.Manager;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaInvalidaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaSinTitularesException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaYaCreadaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.PinInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TarjetaBloqueadaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TokenInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.model.Cliente;
import edu.uclm.esi.iso2.banco20193capas.model.Tarjeta;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaCredito;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaDebito;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTarjetaCredito extends TestCase {
	private Cuenta cuentaPepe;
	private Cliente pepe;
	private TarjetaCredito tcPepe;

	@Before
	public void setUp() {
		Manager.getMovimientoDAO().deleteAll();
		Manager.getMovimientoTarjetaCreditoDAO().deleteAll();
		Manager.getTarjetaCreditoDAO().deleteAll();
		Manager.getTarjetaDebitoDAO().deleteAll();
		Manager.getCuentaDAO().deleteAll();
		Manager.getClienteDAO().deleteAll();

		this.pepe = new Cliente("12345X", "Pepe", "Pérez");
		this.pepe.insert();
		this.cuentaPepe = new Cuenta(1);
		try {
			this.cuentaPepe.addTitular(pepe);
			this.cuentaPepe.insert();
			this.cuentaPepe.ingresar(1000);

			this.tcPepe = this.cuentaPepe.emitirTarjetaCredito(pepe.getNif(), 1000);
			this.tcPepe.cambiarPin(this.tcPepe.getPin(), 1234);
		} catch (Exception e) {
			fail("Excepción inesperada en setUp(): " + e);
		}
	}

	// Valores: -500, 0, 500, 1200
	// Valores: 5678

	@Test
	public void testSacarDinero() { // ImporteInvalidoException
		try {
			tcPepe.sacarDinero(tcPepe.getPin(), -500);
			fail("Esperaba ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testSacarDinero1() { // ImporteInvalidoException
		try {
			tcPepe.sacarDinero(tcPepe.getPin(), 0);
			fail("Esperaba ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testSacarDinero2() { // TestBien
		try {
			tcPepe.sacarDinero(tcPepe.getPin(), 500);
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testSacarDinero3() { // SaldoInsuficiente
		try {
			tcPepe.sacarDinero(tcPepe.getPin(), 1200);
			fail("Esperaba SaldoInsuficienteException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testSacarDinero4() { // PinInvalidoException
		try {
			tcPepe.sacarDinero(1515, 500);
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}
	}

	@Test
	public void testSacarDinero5() { // TarjetaBloqueadaException
		try {
			tcPepe.sacarDinero(1515, 500);
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			tcPepe.sacarDinero(1515, 500);
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			tcPepe.sacarDinero(1515, 500);
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			tcPepe.sacarDinero(tcPepe.getPin(), 500);
			fail("Esperaba TarjetaBloqueadaException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");

		}
	}

	// Valores: -500, 0, 500, 1200
	// Valores: 5678

	@Test
	public void testComprar() { // ImporteInvalidoException
		try {
			tcPepe.comprar(tcPepe.getPin(), -500);
			fail("Esperaba ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testComprar1() { // ImporteInvalidoException
		try {
			tcPepe.comprar(tcPepe.getPin(), 0);
			fail("Esperaba ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testComprar2() { // TestBien
		try {
			tcPepe.comprar(tcPepe.getPin(), 500);
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testComprar3() { // SaldoInsuficiente
		try {
			tcPepe.comprar(tcPepe.getPin(), 1200);
			fail("Esperaba SaldoInsuficienteException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testComprar4() { // PinInvalidoException
		try {
			tcPepe.comprar(1515, 500);
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}
	}

	@Test
	public void testComprar5() { // TarjetaBloqueadaException
		try {
			tcPepe.comprar(1515, 500);
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			tcPepe.comprar(1515, 500);
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			tcPepe.comprar(1515, 500);
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			tcPepe.comprar(tcPepe.getPin(), 500);
			fail("Esperaba TarjetaBloqueadaException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");

		}
	}

	// Valores: 5678

	@Test
	public void testCambiarPin() { // TestBien
		try {
			tcPepe.cambiarPin(tcPepe.getPin(), 5678);
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testCambiarPin1() { // PinInvalidoException
		try {
			tcPepe.cambiarPin(5678, 9123);
			fail("Esperaba PinInvalidoException");
		} catch (PinInvalidoException e) {
		}
	}

	// Valores: -500, 0, 500, 1200
	// Valores: 9123, 1515
	
	@Test
	public void testComprarPorInternet() { // ImporteInvalidoException
		try {
			int token = tcPepe.comprarPorInternet(tcPepe.getPin(), -500);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
			fail("Esperaba ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testComprarPorInternet1() { //ImporteInvalidoException
		try {
			int token = tcPepe.comprarPorInternet(tcPepe.getPin(), 0);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
			assertTrue(token==1234);
			fail("Esperaba ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}
	
	@Test
	public void testComprarPorInternet2() { // TestBien
		try {
			int token = tcPepe.comprarPorInternet(tcPepe.getPin(), 500);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}

	@Test
	public void testComprarPorInternet3() { // SaldoInsuficiente
		try {
			int token = tcPepe.comprarPorInternet(tcPepe.getPin(), 1200);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
			fail("Esperaba SaldoInsuficienteException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}
	
	
	@Test
	public void testComprarPorInternet4() { // PinInvalidoException
		try {
			int token = tcPepe.comprarPorInternet(9123, 500);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}
	}

	@Test
	public void testComprarPorInternet5() { // TarjetaBloqueadaException
		try {
			int token = tcPepe.comprarPorInternet(1515, 500);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
			fail("Esperaba PinInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			int token = tcPepe.comprarPorInternet(1515, 500);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			int token = tcPepe.comprarPorInternet(1515, 500);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
		}

		try {
			int token = tcPepe.comprarPorInternet(1515, 500);
			try {
				tcPepe.confirmarCompraPorInternet(token);
			} catch (TokenInvalidoException e) {
				fail("Se ha producido TokenInvalidoException");
			}
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");

		}
	}

	@Test
	public void testComprarPorInternet6() { // InvalidTokenException
		try {
			tcPepe.comprarPorInternet(tcPepe.getPin(), 500);
			try {
				tcPepe.confirmarCompraPorInternet(6789);
				fail("Esperaba TokenInvalidoException");
			} catch (TokenInvalidoException e) {	
			}
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido TarjetaBloqueadaException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido PinInvalidoException");
		}
	}
	
	@Test
	public void testLiquidar() {
		tcPepe.liquidar();
	}
	
	@Test
	public void testGetCredito(){
		tcPepe.getCredito();
	}
}
