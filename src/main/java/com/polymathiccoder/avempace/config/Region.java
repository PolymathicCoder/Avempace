package com.polymathiccoder.avempace.config;


public enum Region {
	US_EAST_1("US East (Northern Virginia) Region",	"dynamodb.us-east-1.amazonaws.com",	new Protocol[] {Protocol.HTTP, Protocol.HTTPS}),
	US_WSET_1("US West (Northern California) Region", "dynamodb.us-west-1.amazonaws.com", new Protocol[] {Protocol.HTTP, Protocol.HTTPS}),
	US_WEST_2("US West (Oregon) Region", "dynamodb.us-west-2.amazonaws.com", new Protocol[] {Protocol.HTTP, Protocol.HTTPS}),
	EU_WEST_1("EU (Ireland) Region", "dynamodb.eu-west-1.amazonaws.com", new Protocol[] {Protocol.HTTP, Protocol.HTTPS}),
	AP_NORTHEAST_1("Asia Pacific (Tokyo) Region", "dynamodb.ap-northeast-1.amazonaws.com", new Protocol[] {Protocol.HTTP, Protocol.HTTPS}),
	AP_SOUTHEAST_1("Asia Pacific (Singapore) Region", "dynamodb.ap-southeast-1.amazonaws.com", new Protocol[] {Protocol.HTTP, Protocol.HTTPS}),
	AP_SOUTHEAST_2("Asia Pacific (Sydney) Region", "dynamodb.ap-southeast-2.amazonaws.com",	new Protocol[] {Protocol.HTTP, Protocol.HTTPS}),
	SA_EAST_1("South America (Sao Paulo) Region", "dynamodb.sa-east-1.amazonaws.com", new Protocol[] {Protocol.HTTP, Protocol.HTTPS});
	
	// Fields
	private final String prettyName;
	private final String endpoint;
	private final Protocol[] supportedProtocols; 
	
	// Life cycle
	private Region(final String prettyName, final String endpoint, final Protocol[] supportedProtocols) {
		this.prettyName = prettyName;
		this.endpoint = endpoint;
		this.supportedProtocols = supportedProtocols;
	}
	
	// Accessors and mutators
	public String getPrettyName() { return prettyName; }

	public String getEndpoint() { return endpoint; }

	public Protocol[] getSupportedProtocols() { return supportedProtocols; }

	// Types
	public static enum Protocol {
		HTTP, HTTPS;
	}
}
