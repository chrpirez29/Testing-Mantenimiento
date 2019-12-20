package edu.uclm.esi.iso2.banco20193capas;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaInvalidaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.PinInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.model.Cliente;
import edu.uclm.esi.iso2.banco20193capas.model.Cuenta;
import edu.uclm.esi.iso2.banco20193capas.model.Manager;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaCredito;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaDebito;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCuentaConFixtures extends TestCase {
	private Cuenta cuentaPepe, cuentaAna;
	private Cliente pepe, ana;
	private TarjetaDebito tdPepe, tdAna;
	private TarjetaCredito tcPepe, tcAna;

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
		this.ana = new Cliente("98765F", "Ana", "López");
		this.ana.insert();
		this.cuentaPepe = new Cuenta(1);
		this.cuentaAna = new Cuenta(2);
		try {
			this.cuentaPepe.addTitular(pepe);
			this.cuentaPepe.insert();
			this.cuentaPepe.ingresar(1000);
			this.cuentaAna.addTitular(ana);
			this.cuentaAna.insert();
			this.cuentaAna.ingresar(5000);
			this.tcPepe = this.cuentaPepe.emitirTarjetaCredito(pepe.getNif(), 2000);
			this.tcPepe.cambiarPin(this.tcPepe.getPin(), 1234);
			this.tcAna = this.cuentaAna.emitirTarjetaCredito(ana.getNif(), 10000);
			this.tcAna.cambiarPin(this.tcAna.getPin(), 1234);
			this.tdPepe = this.cuentaPepe.emitirTarjetaDebito(pepe.getNif());
			this.tdPepe.cambiarPin(this.tdPepe.getPin(), 1234);
			this.tdAna = this.cuentaAna.emitirTarjetaDebito(ana.getNif());
			this.tdAna.cambiarPin(this.tdAna.getPin(), 1234);
		} catch (Exception e) {
			fail("Excepción inesperada en setUp(): " + e);
		}
	}

	@Test
	public void testImporteInvalido1() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(-1);
	saldoPepe=saldoPepe+(-1);
	this.cuentaPepe.retiroForzoso(-100, "concepto");
	saldoPepe = saldoPepe-(-100);

	fail("Se esperaba ImporteInvalidoException");
	}
	catch (ImporteInvalidoException e) { }
	catch (Exception e) {
	fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
	}
	}
	@Test
	public void testImporteInvalido2() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(0);
	saldoPepe=saldoPepe+(0);
	this.cuentaPepe.retiroForzoso(-100, "concepto");
	saldoPepe = saldoPepe-(-100);

	fail("Se esperaba ImporteInvalidoException");
	}
	catch (ImporteInvalidoException e) { }
	catch (Exception e) {
	fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
	}
	}
	@Test
	public void testNormal3() {
	    try {
	        double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(100);
	saldoPepe=saldoPepe+(100);
	this.cuentaPepe.retiroForzoso(-100, "concepto");
	saldoPepe = saldoPepe-(-100);

	        assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
	        assertTrue(this.cuentaAna.getSaldo()==saldoAna);
	    }
	    catch (Exception e) {
	        fail("Excepción inesperada: " + e);
	    }
	}
	@Test
	public void testNormal4() {
	    try {
	        double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(1000);
	saldoPepe=saldoPepe+(1000);
	this.cuentaPepe.retiroForzoso(-100, "concepto");
	saldoPepe = saldoPepe-(-100);

	        assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
	        assertTrue(this.cuentaAna.getSaldo()==saldoAna);
	    }
	    catch (Exception e) {
	        fail("Excepción inesperada: " + e);
	    }
	}
	@Test
	public void testImporteInvalido5() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(-1);
	saldoPepe=saldoPepe+(-1);
	this.cuentaPepe.retiroForzoso(0, "concepto");
	saldoPepe = saldoPepe-(0);

	fail("Se esperaba ImporteInvalidoException");
	}
	catch (ImporteInvalidoException e) { }
	catch (Exception e) {
	fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
	}
	}
	@Test
	public void testImporteInvalido6() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(-1);
	saldoPepe=saldoPepe+(-1);
	this.cuentaPepe.retiroForzoso(500, "concepto");
	saldoPepe = saldoPepe-(500);

	fail("Se esperaba ImporteInvalidoException");
	}
	catch (ImporteInvalidoException e) { }
	catch (Exception e) {
	fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
	}
	}
	@Test
	public void testImporteInvalido7() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(-1);
	saldoPepe=saldoPepe+(-1);
	this.cuentaPepe.retiroForzoso(1500, "concepto");
	saldoPepe = saldoPepe-(1500);

	fail("Se esperaba ImporteInvalidoException");
	}
	catch (ImporteInvalidoException e) { }
	catch (Exception e) {
	fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
	}
	}
	@Test
	public void testImporteInvalido8() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(0);
	saldoPepe=saldoPepe+(0);
	this.cuentaPepe.retiroForzoso(0, "concepto");
	saldoPepe = saldoPepe-(0);

	fail("Se esperaba ImporteInvalidoException");
	}
	catch (ImporteInvalidoException e) { }
	catch (Exception e) {
	fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
	}
	}
	@Test
	public void testNormal9() {
	    try {
	        double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(100);
	saldoPepe=saldoPepe+(100);
	this.cuentaPepe.retiroForzoso(0, "concepto");
	saldoPepe = saldoPepe-(0);

	        assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
	        assertTrue(this.cuentaAna.getSaldo()==saldoAna);
	    }
	    catch (Exception e) {
	        fail("Excepción inesperada: " + e);
	    }
	}
	@Test
	public void testNormal10() {
	    try {
	        double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(1000);
	saldoPepe=saldoPepe+(1000);
	this.cuentaPepe.retiroForzoso(0, "concepto");
	saldoPepe = saldoPepe-(0);

	        assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
	        assertTrue(this.cuentaAna.getSaldo()==saldoAna);
	    }
	    catch (Exception e) {
	        fail("Excepción inesperada: " + e);
	    }
	}
	@Test
	public void testImporteInvalido11() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(0);
	saldoPepe=saldoPepe+(0);
	this.cuentaPepe.retiroForzoso(500, "concepto");
	saldoPepe = saldoPepe-(500);

	fail("Se esperaba ImporteInvalidoException");
	}
	catch (ImporteInvalidoException e) { }
	catch (Exception e) {
	fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
	}
	}
	@Test
	public void testImporteInvalido12() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(0);
	saldoPepe=saldoPepe+(0);
	this.cuentaPepe.retiroForzoso(1500, "concepto");
	saldoPepe = saldoPepe-(1500);

	fail("Se esperaba ImporteInvalidoException");
	}
	catch (ImporteInvalidoException e) { }
	catch (Exception e) {
	fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
	}
	}
	@Test
	public void testNormal13() {
	    try {
	        double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(100);
	saldoPepe=saldoPepe+(100);
	this.cuentaPepe.retiroForzoso(500, "concepto");
	saldoPepe = saldoPepe-(500);

	        assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
	        assertTrue(this.cuentaAna.getSaldo()==saldoAna);
	    }
	    catch (Exception e) {
	        fail("Excepción inesperada: " + e);
	    }
	}
	@Test
	public void testNormal14() {
	    try {
	        double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(1000);
	saldoPepe=saldoPepe+(1000);
	this.cuentaPepe.retiroForzoso(500, "concepto");
	saldoPepe = saldoPepe-(500);

	        assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
	        assertTrue(this.cuentaAna.getSaldo()==saldoAna);
	    }
	    catch (Exception e) {
	        fail("Excepción inesperada: " + e);
	    }
	}
	@Test
	public void testUnexpectedSituation15() {
	try {
	double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(100);
	saldoPepe=saldoPepe+(100);
	this.cuentaPepe.retiroForzoso(1500, "concepto");
	saldoPepe = saldoPepe-(1500);
	assertTrue(this.cuentaPepe.getSaldo()==-400);
	//fail("Situación inesperada"); ELIMINAMOS ESTE FAIL YA QUE CON EL RETIRO FORZOSO NOS DEBE PERMITIR HACER LA OPERACIÓN
	}
	catch (Exception e) {
	fail("Situación inesperada");
	}
	}
	@Test
	public void testNormal16() {
	    try {
	        double saldoPepe=1000; double saldoAna=5000;
	this.cuentaPepe.ingresar(1000);
	saldoPepe=saldoPepe+(1000);
	this.cuentaPepe.retiroForzoso(1500, "concepto");
	saldoPepe = saldoPepe-(1500);

	        assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
	        assertTrue(this.cuentaAna.getSaldo()==saldoAna);
	    }
	    catch (Exception e) {
	        fail("Excepción inesperada: " + e);
	    }
	}


}
