package net.minecraft.clothutils.plugins.stich;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

/**
 * Core stitch system
 *
 * @author Luminoso-256
 * <p>
 * Steps:
 * 1. Search plugins directory, find all plugin files
 * 2. Note each plugin files hooks and register them to the apropriate hook list
 * 3. Listen to each hook, and on call defer to the plugin
 */
public class StitchLoader {
    public Globals stichGlobals;

    public StitchLoader(Globals StichGlobals) {
        stichGlobals = StichGlobals;
    }


    public void RegisterAllPlugins() {
        Logger logger = Logger.getLogger("Minecraft");
        File PluginsDirectory = new File("plugins");
        String[] PluginFiles = PluginsDirectory.list();
        for (int i = 0; i < PluginFiles.length; i++) {
            logger.info("Found File: " + PluginFiles[i]);

            try {
                RegisterPlugin(PluginFiles[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Takes plugin file, grabs respective json-config file, and registers plugin file to hook lists
     *
     * @param plugin
     */
    private void RegisterPlugin(String plugin) throws IOException { //Yes, i know its impossible for there to not be a file passed in. Its java's fault this is here.
        String PluginFileRaw = "";
        try (Scanner scanner = new Scanner(new File("./plugins/" + plugin), StandardCharsets.UTF_8)) {
            PluginFileRaw = scanner.useDelimiter("\\A").next();
        }


        //  System.out.println("Searching raw file |||"+PluginFileRaw+"||| for hooks ");
        //Time for some good-ol contains operations
        if (PluginFileRaw.contains("OnServerInit")) {
            //    System.out.println("Registering file to OnTestCommandTriggered");
            OnServerInitHook.add(plugin); //Store a ref to the lua file so we can exec it later
        }
    }

    /**
     * Triggers code execution for a given hook
     *
     * @param Hook
     */

    public void CallHook(String Hook, Optional<Entity> Entity, Optional<Block> Block) {
        String output = null;
        Logger logger = Logger.getLogger("Minecraft");

        //Overload/Optional handling
        Entity entity = Entity.isPresent() ? Entity.get() : null;
        Block block = Block.isPresent() ? Block.get() : null;

        //Calling the hook
        switch (Hook) {
            case "OnServerInit":
                for (int i = 0; i < OnServerInitHook.size(); i++)
                    //That gives us paths
                    output = CallFunctionFromLuaFile(OnServerInitHook.get(i), "OnServerInit");
                logger.info("Called Hook: " + Hook + " With result " + output);
                break;
            case "OnBlockDestroy":
                for (int i = 0; i < OnServerInitHook.size(); i++)
                    //That gives us paths
                    output = CallFunctionFromLuaFile(OnBlockDestroyHook.get(i), "OnBlockDestroy");
                logger.info("Called Hook: " + Hook + " With result " + output);
                break;
            case "OnBlockCreate":
                for (int i = 0; i < OnServerInitHook.size(); i++)
                    //That gives us paths
                    output = CallFunctionFromLuaFile(OnBlockCreateHook.get(i), "OnBlockCreate");
                logger.info("Called Hook: " + Hook + " With result " + output);
                break;
        }
    }

    public String CallFunctionFromLuaFile(String filePath, String funcName) {
        //run the lua script defining your function
        LuaValue _G = stichGlobals;
        _G.get("dofile").call(LuaValue.valueOf("./plugins/" + filePath));

        //call the function
        LuaValue Function = _G.get(funcName);

        String ArgType = HookToArgMappings.GetArgsListForHook(funcName);

        LuaValue retvals = null;
        switch (ArgType) {
            case "None":
                retvals = Function.call();
                break;
            case "Block, Entity":
                break;
        }
        //print out the result from the lua function
        return retvals.tojstring(1);
    }


    //Hook Lists
    public List<String> OnServerInitHook = new ArrayList<String>();
    public List<String> OnBlockDestroyHook = new ArrayList<String>();
    public List<String> OnBlockCreateHook = new ArrayList<String>();


}
