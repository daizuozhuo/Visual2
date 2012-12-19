import java.util.Comparator;


public class MyComparator implements Comparator<Occurrence>
{

	@Override
	public int compare(Occurrence o1, Occurrence o2) 
	{
		int timeDiff = o1.getStart() - o2.getStart();
		if (timeDiff != 0)
			return timeDiff;
		
		double valueDiff = o1.getLabel().getValue() - o2.getLabel().getValue();
		
		if (Double.MIN_NORMAL > Math.abs(valueDiff))
			return 0;
		else if (valueDiff > 0)
			return -1;
		else
			return 1;
	}

}
