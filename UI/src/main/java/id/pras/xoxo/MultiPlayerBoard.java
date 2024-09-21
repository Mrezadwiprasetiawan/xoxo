package id.pras.xoxo;
import android.content.Context;
import android.view.MotionEvent;

public class MultiPlayerBoard extends Board{
  public MultiPlayerBoard(Context context){
		super(context,3,3);
	}
	
	public MultiPlayerBoard(Context context, int sideSize, int winSize){
		super(context,sideSize,winSize);
	}
	
	public MultiPlayerBoard(Context context, int sideSize,int winSize, int bg){
	  super(context,sideSize, winSize);
		setBackgroundColor(bg);
	}
	
	public MultiPlayerBoard(Context context, int sideSize,int winSize, int player, int bg){
	  super(context,sideSize, winSize);
	  setCurrentPlayer(player);
		setBackgroundColor(bg);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean parentChangePlayer=event.getAction()==0&&super.onTouchEvent(event);
		if(parentChangePlayer){
			android.widget.Toast.makeText(getContext(),"Score is :"+Evaluator.CalcScore(getBoard(),getWinSize()),0).show();
		}
		return super.onTouchEvent(event);
	}
	
	
}
