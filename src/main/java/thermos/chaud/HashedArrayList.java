package thermos.chaud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class HashedArrayList<TileEntity> implements List<TileEntity>
{

	private ArrayList<TileEntity> stuff = new ArrayList<TileEntity>();
	private HashSet<TileEntity> hashed = new HashSet<TileEntity>();
	
	@Override
	public boolean add(TileEntity arg0)
	{
		boolean flag = hashed.add(arg0);
		
		if (flag)
			stuff.add(arg0);
		
		return flag;
	}

	@Override
	public void add(int arg0, TileEntity arg1)
	{
		boolean flag = hashed.add(arg1);
		
		if (flag)
			stuff.add(arg0, arg1);
	}

	@Override
	public boolean addAll(Collection arg0)
	{
		boolean flag = hashed.addAll(arg0);
		
		if (flag)
			stuff.addAll(arg0);
		
		return flag;
	}

	@Override
	public boolean addAll(int arg0, Collection arg1)
	{
		boolean flag = hashed.addAll(arg1);
		
		if (flag)
			stuff.addAll(arg0, arg1);
		
		return flag;
	}

	@Override
	public void clear()
	{
		this.hashed.clear();
		this.stuff.clear();
	}

	@Override
	public boolean contains(Object arg0)
	{
		return hashed.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection arg0)
	{
		return hashed.containsAll(arg0);
	}

	@Override
	public TileEntity get(int arg0)
	{
		return stuff.get(arg0);
	}

	@Override
	public int indexOf(Object arg0)
	{
		return stuff.indexOf(arg0);
	}

	@Override
	public boolean isEmpty()
	{
		return stuff.isEmpty();
	}

	@Override
	public Iterator<TileEntity> iterator() 
	{
		return this.stuff.iterator();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		if (this.hashed.contains(arg0))
		{
			return this.stuff.lastIndexOf(arg0);
		}
		else 
		{
			return -1;
		}
	}

	@Override
	public ListIterator listIterator() {
		return this.listIterator();
	}

	@Override
	public ListIterator listIterator(int arg0) {
		return this.stuff.listIterator(arg0);
	}

	@Override
	public boolean remove(Object arg0) {
		boolean flag = this.hashed.remove(arg0);
		if (flag)
			this.stuff.remove(arg0);
		return flag;
	}

	@Override
	public TileEntity remove(int arg0) {
		TileEntity te = this.stuff.remove(arg0);
		
		if (te != null)
			hashed.remove(te);
		
		return te;
	}

	@Override
	public boolean removeAll(Collection arg0) {
		boolean flag = this.hashed.removeAll(arg0);
		
		if (flag)
			this.stuff = new ArrayList<TileEntity>(hashed);
		
		return flag;
	}

	@Override
	public boolean retainAll(Collection arg0) {
		boolean flag = this.hashed.retainAll(arg0);
		
		if (flag)
			this.stuff = new ArrayList<TileEntity>(hashed);
		
		return flag;
	}

	@Override
	public TileEntity set(int arg0, TileEntity arg1) 
	{
			TileEntity te = this.stuff.set(arg0, arg1);
			
			if (te != null)
				this.hashed.remove(arg1);
			
			return te;
	}

	@Override
	public int size() {
		return this.stuff.size();
	}

	@Override
	public List<TileEntity> subList(int arg0, int arg1)
	{
		return this.stuff.subList(arg0, arg1);
	}

	@Override
	public Object[] toArray() 
	{
		return this.stuff.toArray();
	}

	@Override
	public Object[] toArray(Object[] arg0) 
	{
		return this.stuff.toArray(arg0);
	}

}
