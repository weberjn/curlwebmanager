package dlm.util;

import java.util.AbstractMap;
import java.util.Set;

public class SystemProperties extends AbstractMap<String, String>
{
	@Override
	public String get(Object name)
	{
		return System.getProperty(name.toString());
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet()
	{
		return null;
	}
}
