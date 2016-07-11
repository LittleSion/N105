package asiabright.n105.Util;

public class MyTiming {
	private boolean state;
	private int num;
	private int hour;
	private int minute;
	private boolean Monday;
	private boolean Tuesday;
	private boolean Wednesday;
	private boolean Thursday;
	private boolean Friday;
	private boolean Saturday;
	private boolean Sunday;
	private String loadStatue;
	private int cmd_num[] = new int[4];

	public MyTiming(String loadStatue) {
		// TODO Auto-generated constructor stub
		this.loadStatue = loadStatue;
		String cmd_1 = loadStatue.substring(0, 2);
		String cmd_2 = loadStatue.substring(2, 4);
		String cmd_3 = loadStatue.substring(4, 6);
		String cmd_4 = loadStatue.substring(6, 8);
		cmd_num[0] = MyList.hexToInt(cmd_1, 2);
		cmd_num[1] = MyList.hexToInt(cmd_2, 2);
		cmd_num[2] = MyList.hexToInt(cmd_3, 2);
		cmd_num[3] = MyList.hexToInt(cmd_4, 2);
		if (MyList.getBitValue(cmd_num[3], 7))
			state = true;
		else
			state = false;
		this.num  = cmd_num[0];
		this.hour=cmd_num[1];
		this.minute = cmd_num[2];
		if (MyList.getBitValue(cmd_num[3], 1))
			Monday = true;
		else
			Monday = false;
		if (MyList.getBitValue(cmd_num[3], 2))
			Tuesday = true;
		else
			Tuesday = false;
	
		if (MyList.getBitValue(cmd_num[3], 3))
			Wednesday = true;
		else
			Wednesday = false;
		if (MyList.getBitValue(cmd_num[3], 4))
			Thursday = true;
		else
			Thursday = false;
		if (MyList.getBitValue(cmd_num[3], 5))
			Friday = true;
		else
			Friday = false;
		if (MyList.getBitValue(cmd_num[3],6))
			Saturday = true;
		else
			Saturday = false;
		if (MyList.getBitValue(cmd_num[3], 0))
			Sunday = true;
		else
			Sunday = false;	
	}

	public boolean isState() {
		return state;
	}

	public String getLoadStatue() {
		return loadStatue;
	}

	public void setState(boolean state) {
		if (MyList.getBitValue(cmd_num[3], 0))
			state = true;
		else
			state = false;
		this.state = state;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		num = cmd_num[0];
		this.num = num;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		hour = cmd_num[1];
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		minute = cmd_num[2];
		this.minute = minute;
	}

	public boolean isMonday() {
		return Monday;
	}

	public void setMonday(boolean monday) {
		if (MyList.getBitValue(cmd_num[3], 1))
			monday = true;
		else
			monday = false;
		Monday = monday;
	}

	public boolean isTuesday() {
		return Tuesday;
	}

	public void setTuesday(boolean tuesday) {
		if (MyList.getBitValue(cmd_num[3], 2))
			tuesday = true;
		else
			tuesday = false;
		Tuesday = tuesday;
	}

	public boolean isWednesday() {
		return Wednesday;
	}

	public void setWednesday(boolean wednesday) {
		if (MyList.getBitValue(cmd_num[3], 3))
			wednesday = true;
		else
			wednesday = false;
		Wednesday = wednesday;
	}

	public boolean isThursday() {
		return Thursday;
	}

	public void setThursday(boolean thursday) {
		if (MyList.getBitValue(cmd_num[3], 4))
			thursday = true;
		else
			thursday = false;
		Thursday = thursday;
	}

	public boolean isFriday() {
		return Friday;
	}

	public void setFriday(boolean friday) {
		if (MyList.getBitValue(cmd_num[3], 4))
			friday = true;
		else
			friday = false;
		Friday = friday;
	}

	public boolean isSaturday() {
		return Saturday;
	}

	public void setSaturday(boolean saturday) {
		if (MyList.getBitValue(cmd_num[3], 6))
			saturday = true;
		else
			saturday = false;
		Saturday = saturday;
	}

	public boolean isSunday() {
		return Sunday;
	}

	public void setSunday(boolean sunday) {
		if (MyList.getBitValue(cmd_num[3], 7))
			sunday = true;
		else
			sunday = false;
		Sunday = sunday;
	}

}
