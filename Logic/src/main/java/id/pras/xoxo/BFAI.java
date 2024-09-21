package id.pras.xoxo;

public class BFAI extends AI{
  private String dataPath;
  private int ThreadCount;
  private boolean hasData=false;
  public BFAI(int[][] board){
		this(board,3,3);
		setThreadCount(1);
	}
	
	public BFAI(int[][] board, int sideSize, int winSize){
		super(board,sideSize,winSize);
	}
	
	public void setThreadCount(int ThreadCount){
		this.ThreadCount=ThreadCount;
	}
	
	public void train(){
		setRunning(true);
		int div=sideSize()/ThreadCount;
		int rem=sideSize()%ThreadCount;
		int first=div+rem;
		
		for(int i=0;i<ThreadCount;i++){
			if(i==1){
				
			}
		}
		
		this.hasData=true;
		setRunning(false);
	}
	
  @Override
  protected void Thinking() {
  	for(int i=0; i<ThreadCount;i++){
		  new Thread(()->{
				if(hasData){
					//implementasi pembacaan data disini
				}else{
					//implementasi kode bruteforcing disini
				}
			});
		}
  }
  
  
}
