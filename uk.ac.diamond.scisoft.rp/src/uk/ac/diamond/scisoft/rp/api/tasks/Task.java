package uk.ac.diamond.scisoft.rp.api.tasks;

import java.util.List;

/**
 * @author vgb98675
 * 
 */
public abstract class Task implements ITask{
	
	protected boolean submitted = false;
	
	@Override
	public boolean isSubmitted() {
		return this.submitted;
	}

	@Override
	public void setTaskAsSubmitted() {
		this.submitted = true;
	}
	
	/**
	 * Gets the contents of the parameter list as one concatenated string
	 * @return string
	 */
	public String getParameterString(){
		String result = "";
		List<String> paramList = getParameterList();
		
		for(int i = 0; i < paramList.size(); i++){
			result += paramList.get(i);
			if(i != paramList.size() - 1){
				result += " ";
			}
		}		
		return result;
	}
	
			

}
