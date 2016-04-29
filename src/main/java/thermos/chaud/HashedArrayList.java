package thermos.chaud;

import java.util.*;

public class HashedArrayList<TileEntity> implements List<TileEntity>
{

	private ArrayList<TileEntity> stuff = new ArrayList<TileEntity>();
	private Set<TileEntity> hashed = Collections.synchronizedSet(new HashSet<TileEntity>());
	
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
		return new HashedArrayIterator(this.stuff.iterator(), this.hashed);
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
		{
			this.stuff = new ArrayList<TileEntity>((int)(hashed.size()*1.5)+1);
			for(TileEntity te : hashed) this.stuff.add(te);
		}
		
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
	
	class HashedArrayIterator<TileEntity> implements Iterator<TileEntity> 
	{
		Iterator<TileEntity> aritr;
		Set<TileEntity> teset;
		public HashedArrayIterator(Iterator aritr, Set<TileEntity> teset)
		{
			this.aritr = aritr;
			this.teset = teset;
		}
		@Override
		public boolean hasNext() {
			return aritr.hasNext();
		}

		private TileEntity last = null;
		
		@Override
		public TileEntity next() {
			last = aritr.next();
			return last;
		}
		@Override
		public void remove()
		{
			aritr.remove();
			teset.remove(last);
		}
	}

	class HashedArrayListIterator<TileEntity> implements ListIterator<TileEntity> 
	{
		ListIterator<TileEntity> aritr;
		HashSet<TileEntity> teset;
		public HashedArrayListIterator(ListIterator<TileEntity> aritr, HashSet<TileEntity> teset)
		{
			this.aritr = aritr;
		}
		@Override
		public void add(TileEntity arg0) {
			boolean flag = teset.add(arg0);
			if (flag)
				this.aritr.add(arg0);
		}
		@Override
		public boolean hasNext() {
			return aritr.hasNext();
		}
		@Override
		public boolean hasPrevious() {
			return aritr.hasPrevious();
		}
		
		private TileEntity lastRet = null;
		
		@Override
		public TileEntity next() {
			lastRet = aritr.next();
			return lastRet;
		}
		@Override
		public int nextIndex() {
			return aritr.nextIndex();
		}
		@Override
		public TileEntity previous() {
			lastRet = aritr.previous();
			return lastRet;
		}
		@Override
		public int previousIndex() {
			return aritr.previousIndex();
		}
		@Override
		public void remove() {
			aritr.remove();
			teset.remove(lastRet);
			
		}
		@Override
		public void set(TileEntity arg0) {
			aritr.set(arg0);
			teset.remove(lastRet);
			teset.add(arg0);
			
		}
	}	
}
