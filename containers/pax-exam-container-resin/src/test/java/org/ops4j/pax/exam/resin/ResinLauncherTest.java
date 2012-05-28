/*
 * Copyright 2012 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.resin;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ops4j.io.FileUtils;
import org.ops4j.pax.exam.zip.ZipInstaller;

import com.caucho.resin.HttpEmbed;
import com.caucho.resin.ResinEmbed;
import com.caucho.resin.WebAppEmbed;
import com.google.common.io.Files;

public class ResinLauncherTest
{
    
    private File tempDir;


    @Before
    public void setUp()
    {
        tempDir = Files.createTempDir();        
    }
    
    @After
    public void tearDown()
    {
        FileUtils.delete( tempDir );        
    }

    
    @Test
    public void launchResin() throws InterruptedException, IOException
    {
        System.setProperty( "java.protocol.handler.pkgs", "org.ops4j.pax.url" );
        ResinEmbed resin = new ResinEmbed();
        HttpEmbed http = new HttpEmbed(9080);
        File webappDir = new File(tempDir, "wicket-examples");
        URL applUrl = new URL("mvn:org.apache.wicket/wicket-examples/1.5.3/war");
        ZipInstaller installer = new ZipInstaller( applUrl, webappDir.getAbsolutePath() );
        installer.downloadAndInstall();
        WebAppEmbed webApp = new WebAppEmbed("/wicket-examples", webappDir.getAbsolutePath());
        resin.addPort(http);
        resin.start();

        resin.addWebApp( webApp );
        resin.removeWebApp( webApp );
        resin.stop();        
    }
}
