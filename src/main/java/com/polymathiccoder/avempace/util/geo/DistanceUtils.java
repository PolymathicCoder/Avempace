package com.polymathiccoder.avempace.util.geo;

import javax.measure.unit.NonSI;

import org.jscience.geography.coordinates.LatLong;

import com.polymathiccoder.avempace.config.Region;
import com.polymathiccoder.avempace.util.DnsUtils;
import com.polymathiccoder.avempace.util.IPUtils;
import com.polymathiccoder.avempace.util.IPUtils.GeoLocation;

public class DistanceUtils {
	public static void main(String[] args) {
		System.out.println("REGION -----------");
		final String regionHostname = Region.SA_EAST_1.getEndpoint();
		final String regionIP = DnsUtils.lookup(regionHostname);
		final GeoLocation regionGeoLocation = IPUtils.lookupGeoLocation(regionIP);
		System.out.println(regionGeoLocation);

		System.out.println("YOU --------------");
		final String myPublicIP = IPUtils.lookupPublicIP();
		final GeoLocation myGeoLocation = IPUtils.lookupGeoLocation(myPublicIP);
		System.out.println(myGeoLocation);

		System.out.println(calculateOrthodromicDistance(regionGeoLocation, myGeoLocation, DistanceCalculationFormula.HAVERSINE));
		System.out.println(calculateOrthodromicDistance(regionGeoLocation, myGeoLocation, DistanceCalculationFormula.SPHERICAL_LAW_OF_COSINES));
		System.out.println(calculateOrthodromicDistance(regionGeoLocation, myGeoLocation, DistanceCalculationFormula.EQUIRECTANGULAR_APPROXIMATION_WITH_PYTHAGOREAN_THEOREM));
	}

// Static fields
	private static final int EARTHS_RADIUS_IN_KM = 6371;

// Static behavior
	/**
	 * OrthodromicDistance: http://en.wikipedia.org/wiki/Great-circle_distance
	 */
	public static double calculateOrthodromicDistance(final GeoLocation geoLocation1, final GeoLocation geoLocation2, final DistanceCalculationFormula usingFormula) {
		double distanceInKm = 0;
		switch (usingFormula) {
			case HAVERSINE:
				distanceInKm = calculateOrthodromicDistanceUsingHaversineFormula(geoLocation1, geoLocation2);
				break;
			case SPHERICAL_LAW_OF_COSINES:
				distanceInKm = calculateOrthodromicDistanceUsingSphericalLawOfCosinesFormula(geoLocation1, geoLocation2);
				break;
			case EQUIRECTANGULAR_APPROXIMATION_WITH_PYTHAGOREAN_THEOREM:
				distanceInKm = calculateOrthodromicDistanceUsingEquirectangularApproximationWithPythagoreanTheoremFormula(geoLocation1, geoLocation2);
				break;
		default:
			//TODO Handle better
			break;
		}
		return distanceInKm;
	}

	// Helpers
	private static double calculateOrthodromicDistanceUsingHaversineFormula(final GeoLocation geoLocation1, final GeoLocation geoLocation2) {
		final LatLong latLong1 = LatLong.valueOf(geoLocation1.getLatitude(), geoLocation1.getLongitude(), NonSI.DEGREE_ANGLE);
		final LatLong latLong2 = LatLong.valueOf(geoLocation2.getLatitude(), geoLocation2.getLongitude(), NonSI.DEGREE_ANGLE);

		final double deltaLatInRad = latLong2.latitudeValue(NonSI.CENTIRADIAN) - latLong1.latitudeValue(NonSI.CENTIRADIAN);
		final double deltaLongInRad = latLong2.longitudeValue(NonSI.CENTIRADIAN) - latLong1.longitudeValue(NonSI.CENTIRADIAN);
		final double lat1InRad = latLong1.latitudeValue(NonSI.CENTIRADIAN);
		final double lat2InRad = latLong2.latitudeValue(NonSI.CENTIRADIAN);

		final double x = Math.sin(deltaLatInRad / 2) * Math.sin(deltaLatInRad / 2) +
				Math.sin(deltaLongInRad / 2) * Math.sin(deltaLongInRad / 2) * Math.cos(lat1InRad) * Math.cos(lat2InRad);
		final double d = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
		final double distanceInKm = d * EARTHS_RADIUS_IN_KM;

		return distanceInKm;
	}

	private static double calculateOrthodromicDistanceUsingSphericalLawOfCosinesFormula(final GeoLocation geoLocation1, final GeoLocation geoLocation2) {
		final LatLong latLong1 = LatLong.valueOf(geoLocation1.getLatitude(), geoLocation1.getLongitude(), NonSI.DEGREE_ANGLE);
		final LatLong latLong2 = LatLong.valueOf(geoLocation2.getLatitude(), geoLocation2.getLongitude(), NonSI.DEGREE_ANGLE);

		final double lat1InRad = latLong1.latitudeValue(NonSI.CENTIRADIAN);
		final double long1InRad = latLong1.longitudeValue(NonSI.CENTIRADIAN);

		final double lat2InRad = latLong2.latitudeValue(NonSI.CENTIRADIAN);
		final double long2InRad = latLong2.longitudeValue(NonSI.CENTIRADIAN);

		final double d = Math.acos(
				Math.sin(lat1InRad) * Math.sin(lat2InRad)
				+
                Math.cos(lat1InRad) * Math.cos(lat2InRad) * Math.cos(long2InRad - long1InRad));
		final double distanceInKm = d * EARTHS_RADIUS_IN_KM;

		return distanceInKm;
	}

	private static double calculateOrthodromicDistanceUsingEquirectangularApproximationWithPythagoreanTheoremFormula(final GeoLocation geoLocation1, final GeoLocation geoLocation2) {
		final LatLong latLong1 = LatLong.valueOf(geoLocation1.getLatitude(), geoLocation1.getLongitude(), NonSI.DEGREE_ANGLE);
		final LatLong latLong2 = LatLong.valueOf(geoLocation2.getLatitude(), geoLocation2.getLongitude(), NonSI.DEGREE_ANGLE);

		final double lat1InRad = latLong1.latitudeValue(NonSI.CENTIRADIAN);
		final double long1InRad = latLong1.longitudeValue(NonSI.CENTIRADIAN);
		final double lat2InRad = latLong2.latitudeValue(NonSI.CENTIRADIAN);
		final double long2InRad = latLong2.longitudeValue(NonSI.CENTIRADIAN);

		final double d = Math.sqrt(
			Math.pow((long2InRad - long1InRad) * Math.cos((lat1InRad + lat2InRad) / 2), 2)
					+
			Math.pow((lat2InRad - lat1InRad), 2)
		);
		final double distanceInKm = d * EARTHS_RADIUS_IN_KM;

		return distanceInKm;
	}

// Types
	public static enum DistanceCalculationFormula {
		HAVERSINE,
		SPHERICAL_LAW_OF_COSINES,
		EQUIRECTANGULAR_APPROXIMATION_WITH_PYTHAGOREAN_THEOREM
	}
}
