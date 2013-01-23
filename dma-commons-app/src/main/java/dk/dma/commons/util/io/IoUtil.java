/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.commons.util.io;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Kasper Nielsen
 */
public class IoUtil {

    public static Path addTimestamp(Path base, TimeUnit unit) {
        long now = System.currentTimeMillis();
        String filename = base.getFileName().toString();
        int i = filename.indexOf('.');
        String postfix = addTimeStampGetPostFix(new Date(now), unit);
        if (i > 0) {
            filename = filename.substring(0, i) + "-" + postfix + filename.substring(i);
        } else {
            filename = filename + "-" + postfix;
        }
        return base.getParent().resolve(filename);
    }

    public static String addTimeStampGetPostFix(Date now, TimeUnit unit) {
        if (unit == TimeUnit.MINUTES) {
            // does not handle daylight saving properties
            // Ogsaa lige et problem hvis den allerede har vaere aabnet
            // og saa bliver service genstarted
            return new SimpleDateFormat("yyyy-MM-dd_HH:mm").format(now);
        } else if (unit == TimeUnit.HOURS) {
            return new SimpleDateFormat("yyyy-MM-dd_HH").format(now);
        } else if (unit == TimeUnit.DAYS) {
            return new SimpleDateFormat("yyyy-MM-dd").format(now);
        } else {
            throw new IllegalArgumentException(unit.toString());
        }
    }

    public static OutputStream notCloseable(OutputStream os) {
        return new FilterOutputStream(os) {
            /** {@inheritDoc} */
            @Override
            public void close() throws IOException {
                throw new UnsupportedOperationException("Close is not supported");
            }
        };
    }

    public static final void validateFolderExist(String parameterName, File folder) {
        if (!folder.exists()) {
            System.err.println(parameterName + " folder does not exist, " + parameterName + " = " + folder);
            System.exit(1);
        } else if (!folder.exists()) {
            System.err.println("Specified " + parameterName + " is not a folder, " + parameterName + " = " + folder);
            System.exit(1);
        }
    }
}