package IFTTT;


public class filedemo extends Thread{
	FileList fileshell;
	filedemo(FileList filelist){
		fileshell = filelist;
	}
	
	public void run(){
		String path = "f:\\movie\\test";
		//get file
		//fileshell is the only way to get file
		safe_file file = fileshell.get_shell(path);
		//get file information
		//if file not exits still return
		//1.get fileName
		System.out.println(file.getName());
		//2.get filePath
		System.out.println(file.getPath());
		//3.get fileSize
		//if isFile return length; if isDirectory return 0
		//you can see size of directory when program run
		System.out.println(file.getlength());
		//4.get lastmodified
		System.out.println(file.lastModified());
		
		try {
			sleep(5000);
		} catch (InterruptedException e) {}
		
		//create new file or new directory
		//if file or dir exits output file exits
		file.createNew("File");
		file.createNew("directory");
		//if path wrong or other reasons output flase
		file = fileshell.get_shell("f:\\movie\\test\\2\\1.txt");
		file.createNew("File");//no dir [2] so output false
		//if path correct create success
		file = fileshell.get_shell("f:\\movie\\test\\666.txt");
		file.createNew("File");
		file = fileshell.get_shell("f:\\movie\\test\\233");
		file.createNew("Directory");
		
		try {
			sleep(10000);
		} catch (InterruptedException e) {}
		
		//delete file or directory
		file = fileshell.get_shell("f:\\movie\\test\\666.txt");
		file.delete();
		file = fileshell.get_shell("f:\\movie\\test\\233");
		file.delete();
		//if file not exits return false
		
		
		try {
			sleep(5000);
		} catch (InterruptedException e) {}
		
		//file write, rename, move
		//you need fileshell do not use other operation
		file = fileshell.get_shell("f:\\movie\\test\\666.txt");
		file.createNew("File");
		//write
		String content = "hello movie";
		fileshell.write(file, content, true);//flag is true then content add to end
		//rename
		
		fileshell.rename(file, "2333.txt");
		System.out.println(file.getName());
		//move
		
		fileshell.moveTo(file, "f:\\movie");
		System.out.println(file.getPath());
		
		
	}
	
	
	
}
