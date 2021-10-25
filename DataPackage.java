package tk.bobbzorzen;

import java.io.Serializable;

public class DataPackage implements Serializable
{
	private static final long serialVersionUID = 1L;
	DataPackage()
	{
		this.oldPos = 9;
		this.newPos = 9;
	}
	public int getOldPos()
	{
		return oldPos;
	}
	public void setOldPos(int oldPos)
	{
		this.oldPos = oldPos;
	}
	public int getNewPos()
	{
		return newPos;
	}
	public void setNewPos(int newPos)
	{
		this.newPos = newPos;
	}
	public int oldPos = 0;
	public int newPos = 0;
}
