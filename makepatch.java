import java.util.*;
import java.io.*;
public class makepatch
{

public static void main(String[] args) throws Exception
{

if(args.length == 1)
{

System.out.println("[JPATCH] Patching the patch!");
String post = "";
Scanner in = new Scanner(new File(args[0]));
in.nextLine();in.nextLine();
post += in.nextLine().replace("\\","/").replace("\\","/").replace("\"","").replace("//","/").replace("eclipse/Clean/src/main/java","../src-base/minecraft").replace("eclipse\\\\Clean\\\\src\\\\main\\\\java\\\\","../src-base/minecraft/")+"\n";
post += in.nextLine().replace("\\\\","/").replace("\\","/").replace("\"","").replace("//","/").replace("eclipse/cauldron/src/main/java","../src-work/minecraft").replace("eclipse/Cauldron/src/main/java","../src-work/minecraft").replace("eclipse\\\\Cauldron\\\\src\\\\main\\\\java\\\\","../src-work/minecraft")+"\n";
while(in.hasNextLine())
{

	String current = in.nextLine();
	if(current.contains("No newline at end of file")) continue;

	if(current.startsWith("@@"))
	{
		if(!(current.endsWith("@@") || current.endsWith("@ ")))
		{
			post += current.substring(0,current.lastIndexOf("@@")+2)+"\n";
			//post += current.substring(current.lastIndexOf("@@")+2)+"\n";
		}
		else
		{
			post += current + "\n";
		}

	}
	else
	{
		post += current + "\n";
	}

}
in.close();
PrintWriter out = new PrintWriter(new File(args[0]));
out.print(post);
out.close();
System.out.println("[JPATCH] Patch is patched!");

}
else if(args.length == 2)
{
if(args[0].equalsIgnoreCase("chop"))
{
System.out.println(args[1].replace("eclipse/cauldron/src/main/java/","").replace("eclipse/Cauldron/src/main/java/","").replace("eclipse\\cauldron\\src\\main\\java\\","").replace("eclipse\\Cauldron\\src\\main\\java\\",""));

return;
}
else if(args[0].equalsIgnoreCase("dir"))
{
System.out.println(args[1].substring(0,args[1].lastIndexOf("/")));
}
}
else
System.out.println("You gave : " + args.length+" args. Usage: java makepatch FILENAME / chop ECLIPSE_CAULDRON_NAME");

}

}
