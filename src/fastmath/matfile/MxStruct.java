/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import fastmath.matfile.MiElement;
import fastmath.matfile.MiInt32;
import fastmath.matfile.MiInt8;
import fastmath.matfile.MxClass;
import fastmath.matfile.NamedWritable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MxStruct
extends MxClass
implements NamedWritable {
    public static final int mxSTRUCT_CLASS = 2;
    private MiInt32 fieldNameLengthArray;
    private final List<String> fieldNames = new ArrayList<String>();
    private MiInt8 fieldNamesArray;
    private final TreeMap<String, MiElement> fields = new TreeMap();
    private String name;
    private boolean needsRefresh;

    public MxStruct() {
        super(null);
    }

    @SafeVarargs
    public <W extends MiElement> MxStruct(String name, W ... elements) {
        super(null);
        this.setName(name);
        Arrays.asList(elements).forEach(element -> this.addField(element.getName(), (MiElement)element));
    }

    protected MxStruct(Iterator<MiElement> iter) {
        super(null);
        MiElement element = iter.next();
        this.fieldNameLengthArray = (MiInt32)element;
        assert (this.fieldNameLengthArray.getSize() != 1) : "Field Name Length should be a 1 Int32 value. See Page 25 of MAT-File Format Documentation";
        int fieldNameLength = this.fieldNameLengthArray.elementAt(0);
        this.fieldNamesArray = (MiInt8)element;
        int numFields = (int)this.fieldNamesArray.numBytes(0L) / fieldNameLength;
        if (this.fieldNamesArray.numBytes(0L) % (long)fieldNameLength != 0L) {
            throw new UnsupportedOperationException("fieldNames length invalid");
        }
        for (int i = 0; i < numFields; ++i) {
            String fieldName = this.fieldNamesArray.asString().substring(i * fieldNameLength, (i + 1) * fieldNameLength).trim();
            this.fieldNames.add(fieldName);
            this.fields.put(fieldName, iter.next());
        }
        this.needsRefresh = false;
    }

    @Override
    public MxClass.Type getArrayType() {
        return MxClass.Type.STRUCT;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long numBytes(long pos) {
        long startPos = pos;
        if (this.needsRefresh) {
            this.refresh();
        }
        long a = this.fieldNameLengthArray.totalSize(pos);
        pos += a;
        a = this.fieldNamesArray.totalSize(pos);
        pos += a;
        for (MiElement element : this.fields.values()) {
            a = element.totalSize(pos);
            pos += a;
        }
        return pos - startPos;
    }

    public void put(String key, MiElement value) {
        if (!this.fields.containsKey(key)) {
            this.fieldNames.add(key);
        }
        this.fields.put(key, value);
        this.needsRefresh = true;
    }

    private void refresh() {
        int maxFieldNameSize = this.fields.keySet().stream().mapToInt(name -> name.length()).max().orElse(0);
        this.fieldNameLengthArray = new MiInt32(1);
        this.fieldNameLengthArray.setElementAt(0, maxFieldNameSize);
        StringBuffer fieldNameBuffer = new StringBuffer();
        for (String fieldName : this.fieldNames) {
            fieldNameBuffer.append(fieldName);
            int fieldNameLength = fieldName.length();
            if (fieldNameLength >= maxFieldNameSize) continue;
            for (int i = 0; i < maxFieldNameSize - fieldNameLength; ++i) {
                fieldNameBuffer.append("\u0000");
            }
        }
        this.fieldNamesArray = new MiInt8(fieldNameBuffer.toString());
        this.needsRefresh = false;
    }

    public void addField(String name, MiElement value) {
        this.fields.put(name, value);
        this.needsRefresh = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "mxStruct " + this.fields;
    }

    @Override
    public void write(SeekableByteChannel channel) throws IOException {
        if (this.needsRefresh) {
            this.refresh();
        }
        this.fieldNameLengthArray.write(channel);
        this.fieldNamesArray.write(channel);
        for (String fieldName : this.fieldNames) {
            MiElement element = this.fields.get(fieldName);
            element.write(channel);
        }
    }
}

