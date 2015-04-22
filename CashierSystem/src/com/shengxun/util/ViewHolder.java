package com.shengxun.util;
import android.util.SparseArray;
import android.view.View;
/**
 * @title ViewHolder工具类
 * @description ViewHolder模式的一种简洁写法,相对与传统的写法：
 * @NO1 ViewHolder Pattern是每一个View一个ViewHolder，里面是记录listitem内控件引用；
 *  	SparseArray是记录id和控件引用的键值对
 * 		，当然SparseArray初始化会分配一定地址空间（默认是10），但是这些内存损耗应该是可以忽略的~
 * @NO2 SparseArray是优化过的int-object的hashmap，使用折半查找，并优化了内存分配，所以代码执行成本通常是可控的。 
 *      综上，使用SparseArray与ViewHolder相比是有损耗，但应该是可用的使用SparseArray优点是加快开发速度和减少代码维护
 *      ，当发现有性能瓶颈时，再进行优化
 *                                                                        ~
 * @createDate 2014-01-20
 * @author LILINQ
 * 
 */
public class ViewHolder {
	
	/**
	 * 获取ViewHolder中的控件
	 * 
	 * @param view
	 *            高级组件(ListView,GridView...)的行布局
	 * @param id
	 *            行布局中某个组件的id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		// SparseArray是优化过的存储integer和object键值对的hashmap
		SparseArray<View> viewHolder = (SparseArray<View>)view.getTag();
		// 如果没有SparseArray这个键值对，对行布局设置Tag，Tag为一个Sparse(理解为HashMap<Integer,View>)
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		//获取键值对当中的一个组件,如果没有先进行放入
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;

	}

}
