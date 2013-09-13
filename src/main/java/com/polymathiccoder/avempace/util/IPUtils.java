package com.polymathiccoder.avempace.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.City;
import com.polymathiccoder.avempace.util.error.UtilsException;

public final class IPUtils {
// Static fields
	private static final String VALID_IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private static final File GEO_IP_CITY_DB_FILE = new File("/Users/abdel/Dev/workspace/sts/nimble/src/main/resources/META-INF/geoIPDB/GeoLite2-City.mmdb");
	@SuppressWarnings("unused")
	private static final File GEO_IP_COUNTRY_DB_FILE = new File("/Users/abdel/Dev/workspace/sts/nimble/src/main/resources/META-INF/geoIPDB/GeoLite2-City.mmdb");

	private static URL PUBLIC_IP_LOOKUP_SERVICE_URL;
	static {
		try {
			PUBLIC_IP_LOOKUP_SERVICE_URL = new URL("http://checkip.amazonaws.com");
		} catch (MalformedURLException e) {
        	throw new IpUtilsException(
        			String.format(IpUtilsException.ERROR_PUBLIC_IP__UNREACHABLE_SERVICE));
		}
	}

// Static behavior
    public static String lookupPublicIP() {
        try (
        	final InputStreamReader inputStreamReader = new InputStreamReader(PUBLIC_IP_LOOKUP_SERVICE_URL.openStream());
        	final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ) {
            return bufferedReader.readLine();
        } catch (final IOException ioException) {
        	throw new IpUtilsException(
        			String.format(IpUtilsException.ERROR_PUBLIC_IP__UNREACHABLE_SERVICE));
        }
    }

    public static boolean validateIP(final String ip) {
    	final Pattern pattern = Pattern.compile(VALID_IP_PATTERN);
    	final Matcher matcher = pattern.matcher(ip);
    	return matcher.matches();
    }

    public static GeoLocation lookupGeoLocation(final String ip) {
		try (
			final DatabaseReader databaseReader = new DatabaseReader(GEO_IP_CITY_DB_FILE);
		) {
			final City city = databaseReader.city(InetAddress.getByName(ip));
			return new GeoLocation(city.getLocation().getLatitude(), city.getLocation().getLongitude());
		} catch (final IOException ioException) {
			throw new IpUtilsException(
        			String.format(IpUtilsException.ERROR_GEO_IP__UNREACHABLE_SERVICE));
		} catch (final GeoIp2Exception geoIp2Exception) {
			throw new IpUtilsException(
					String.format(IpUtilsException.ERROR_GEO_IP__NO_RESOLUTION_OF_IP_TO_GEO_LOCATION, ip));
		}
	}

// Types
    @SuppressWarnings("serial")
	public static final class IpUtilsException extends UtilsException {
    // Static fields
        public static final String ERROR_PUBLIC_IP__UNREACHABLE_SERVICE = "Public IP Resolution: Service is unreachable";
        public static final String ERROR_GEO_IP__UNREACHABLE_SERVICE = "Geo IP Resolution: Service is unreachable";
        public static final String ERROR_GEO_IP__NO_RESOLUTION_OF_IP_TO_GEO_LOCATION = "Geo IP Resolution: Could not resolve the IP '%s' to a location";

    // Life cycle
    	private IpUtilsException(final String message) {
    		super(message);
    	}

    	private IpUtilsException(final String message, final Throwable cause) {
    		super(message, cause);
    	}
    }

    @AutoProperty
    public static class GeoLocation {
    // Fields
    	private final double latitude;
    	private final double longitude;

    // Life cycle
    	public GeoLocation(final double latitude, final double longitude) {
    		this.latitude = latitude;
    		this.longitude = longitude;
    	}

    // Accessors and mutators
    	public double getLatitude() { return latitude; }

    	public double getLongitude() { return longitude; }

    // Common methods
    	@Override
    	public boolean equals(final Object other) { return Pojomatic.equals(this, other); }
    	@Override
    	public int hashCode() { return Pojomatic.hashCode(this); }
    	@Override
    	public String toString() { return Pojomatic.toString(this); }
    }
}
