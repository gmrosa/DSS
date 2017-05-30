package br.com.furb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.javatuples.Triplet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AuthTest {

    @Parameters
    public static Collection<Triplet<String, String, String>> data() throws Throwable {
	List<Triplet<String, String, String>> triplets = new ArrayList<>();

	for (int i = 0; i < 100; i++) {
	    String salt = UUID.randomUUID().toString();
	    String password = "senhateste" + i;
	    String secret = salt + password;

	    String hash = Sha256.getHash(secret);

	    triplets.add(new Triplet<>(salt, password, hash));
	}
	return triplets;
    }

    private Triplet<String, String, String> triplet;

    public AuthTest(Triplet<String, String, String> triplet) {
	this.triplet = triplet;
    }

    @Test
    public void test() throws Throwable {
	String actual = Sha256.getHash(triplet.getValue0() + triplet.getValue1());
	String expected = triplet.getValue2();

	Assert.assertTrue(expected.equals(actual));
    }

}
