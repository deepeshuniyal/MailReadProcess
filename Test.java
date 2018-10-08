class Test
{
	public static void main(String a[]) {
		String at = "text./mp4";
		System.out.println(at.lastIndexOf("mp4"));
		at = at.substring(0,at.indexOf("/mp4"))+".mp3";
		System.out.println(at);
	}
}
