package com.github.terma.gigaspacesqlconsole.provider;

import com.gigaspaces.client.iterator.IteratorScope;
import com.gigaspaces.document.SpaceDocument;
import com.github.terma.gigaspacesqlconsole.core.ExportRequest;
import com.j_spaces.core.admin.JSpaceAdminProxy;
import com.j_spaces.core.client.GSIterator;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.IteratorBuilder;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Exporter {

    private final static int BUFFER_SIZE = 1000;

    public static void execute(final ExportRequest request, final OutputStream outputStream) throws Exception {
        final GigaSpace gigaSpace = GigaSpaceUtils.getGigaSpace(request);

        final List<String> types = getTypesForExport(request, gigaSpace);

        final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        for (final String type : types) {
            final GSIterator iterator = new IteratorBuilder(gigaSpace)
                    .addTemplate(new SpaceDocument(type))
                    .bufferSize(BUFFER_SIZE)
                    .iteratorScope(IteratorScope.CURRENT).create();

            final ZipEntry zipEntry = new ZipEntry(type + ".ser");
            zipOutputStream.putNextEntry(zipEntry);

            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(zipOutputStream);
            while (iterator.hasNext()) {
                final Object spaceDocument = iterator.next();
                objectOutputStream.writeObject(spaceDocument);
            }

            objectOutputStream.flush();
            zipOutputStream.closeEntry();
        }

        zipOutputStream.close();
    }

    private static List<String> getTypesForExport(
            final ExportRequest request, final GigaSpace gigaSpace) throws RemoteException {
        return request.types != null ? request.types : findTypesForExport(gigaSpace);
    }

    private static List<String> findTypesForExport(final GigaSpace gigaSpace) throws RemoteException {
        final JSpaceAdminProxy admin = (JSpaceAdminProxy) gigaSpace.getSpace().getAdmin();
        return admin.getRuntimeInfo().m_ClassNames;
    }

}
