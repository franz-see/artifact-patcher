Artifact Patcher
================

Tool for patching ZIPs/JARs/EARs/WARs. Useful for transferring just an artifact patch instead of a huge new artifact over the network.

How this works:
----------------
Given and old.jar and a new.jar, if you want to patch the old.jar so that it looks like new.jar, you would need to do the following:
1. Create a description of old.jar
2. Create a patch out of old.jar's description and new.jar
3. Apply patch to old.jar

Commands:
----------------
Create a description of old.jar (generates ```old.xml```): 
`java -jar artifact-patcher-exec.jar describe -f old.jar`

Create a patch out of old.jar's description and new.jar (generates ```new.patch```): 
`java -jar artifact-patcher-exec.jar diff -d old.xml -f new.jar`

Apply patch to old.jar (generates ```old.patched.jar```): 
`java -jar artifact-patcher-exec.jar patch -f old.jar -p new.patch`

FAQ:
----------------
Q: What's a Description file?
A: An XML describing an artifact - its contents and its checksums

Q: Why do we need a Description file?
A: Because the ```old.jar``` and the ```new.jar``` may not necessarily be in the same server. If the ```old.jar``` is in the remote server, you can just copy ```artifact-patcher-exec.jar``` on the remote server, generate the Description file ```old.xml```, copy the description file back to your local (where your ```new.jar```), generate ```new.patch``` from your local server, and copy the ```new.patch``` to the remote server to be applied to ```old.jar```. This prevents the copying of the whole of ```old.jar``` or ```new.jar``` back and forth through the network.
