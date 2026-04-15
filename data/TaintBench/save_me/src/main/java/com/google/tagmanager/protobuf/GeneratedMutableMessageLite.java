package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.GeneratedMessageLite.ExtendableBuilder;
import com.google.tagmanager.protobuf.GeneratedMessageLite.GeneratedExtension;
import com.google.tagmanager.protobuf.Internal.EnumLite;
import com.google.tagmanager.protobuf.MessageLite.Builder;
import com.google.tagmanager.protobuf.WireFormat.FieldType;
import com.google.tagmanager.protobuf.WireFormat.JavaType;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class GeneratedMutableMessageLite<MessageType extends GeneratedMutableMessageLite<MessageType>> extends AbstractMutableMessageLite implements Serializable {
    private static final long serialVersionUID = 1;
    protected ByteString unknownFields = ByteString.EMPTY;

    static final class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private byte[] asBytes;
        private String messageClassName;

        SerializedForm(MutableMessageLite regularForm) {
            this.messageClassName = regularForm.getClass().getName();
            this.asBytes = regularForm.toByteArray();
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws ObjectStreamException {
            try {
                MutableMessageLite message = (MutableMessageLite) Class.forName(this.messageClassName).getMethod("newMessage", new Class[0]).invoke(null, new Object[0]);
                if (message.mergeFrom(CodedInputStream.newInstance(this.asBytes))) {
                    return message;
                }
                throw new RuntimeException("Unable to understand proto buffer");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find proto buffer class", e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException("Unable to find newMessage method", e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException("Unable to call newMessage method", e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("Error calling newMessage", e4.getCause());
            }
        }
    }

    public static abstract class ExtendableMutableMessage<MessageType extends ExtendableMutableMessage<MessageType>> extends GeneratedMutableMessageLite<MessageType> {
        /* access modifiers changed from: private */
        public FieldSet<ExtensionDescriptor> extensions = FieldSet.emptySet();

        protected class ExtensionWriter {
            private final Iterator<Entry<ExtensionDescriptor, Object>> iter;
            private final boolean messageSetWireFormat;
            private Entry<ExtensionDescriptor, Object> next;

            private ExtensionWriter(boolean messageSetWireFormat) {
                this.iter = ExtendableMutableMessage.this.extensions.iterator();
                if (this.iter.hasNext()) {
                    this.next = (Entry) this.iter.next();
                }
                this.messageSetWireFormat = messageSetWireFormat;
            }

            public void writeUntil(int end, CodedOutputStream output) throws IOException {
                while (this.next != null && ((ExtensionDescriptor) this.next.getKey()).getNumber() < end) {
                    ExtensionDescriptor extension = (ExtensionDescriptor) this.next.getKey();
                    if (this.messageSetWireFormat && extension.getLiteJavaType() == JavaType.MESSAGE && !extension.isRepeated()) {
                        output.writeMessageSetExtension(extension.getNumber(), (MessageLite) this.next.getValue());
                    } else {
                        FieldSet.writeField(extension, this.next.getValue(), output);
                    }
                    if (this.iter.hasNext()) {
                        this.next = (Entry) this.iter.next();
                    } else {
                        this.next = null;
                    }
                }
            }
        }

        public /* bridge */ /* synthetic */ MessageLite getDefaultInstanceForType() {
            return GeneratedMutableMessageLite.super.getDefaultInstanceForType();
        }

        protected ExtendableMutableMessage() {
        }

        /* access modifiers changed from: 0000 */
        public void internalSetExtensionSet(FieldSet<ExtensionDescriptor> extensions) {
            this.extensions = extensions;
        }

        public MessageType clear() {
            assertMutable();
            this.extensions = FieldSet.emptySet();
            return (ExtendableMutableMessage) GeneratedMutableMessageLite.super.clear();
        }

        private void ensureExtensionsIsMutable() {
            if (this.extensions.isImmutable()) {
                this.extensions = this.extensions.clone();
            }
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> extension) {
            if (extension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> extension) {
            verifyExtensionContainingType(extension);
            return this.extensions.hasField(extension.descriptor);
        }

        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> extension) {
            verifyExtensionContainingType(extension);
            return this.extensions.getRepeatedFieldCount(extension.descriptor);
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> extension) {
            verifyExtensionContainingType(extension);
            Object value = this.extensions.getField(extension.descriptor);
            if (value == null) {
                return extension.defaultValue;
            }
            if (extension.descriptor.isRepeated) {
                return Collections.unmodifiableList((List) extension.fromFieldSetType(value));
            }
            return extension.fromFieldSetType(value);
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> extension, int index) {
            verifyExtensionContainingType(extension);
            return extension.singularFromFieldSetType(this.extensions.getRepeatedField(extension.descriptor, index));
        }

        public final <Type extends MutableMessageLite> Type getMutableExtension(GeneratedExtension<MessageType, Type> extension) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            ExtensionDescriptor descriptor = extension.descriptor;
            if (descriptor.getLiteJavaType() != JavaType.MESSAGE) {
                throw new UnsupportedOperationException("getMutableExtension() called on a non-Message type.");
            } else if (descriptor.isRepeated()) {
                throw new UnsupportedOperationException("getMutableExtension() called on a repeated type.");
            } else {
                Object value = this.extensions.getField(extension.descriptor);
                if (value != null) {
                    return (MutableMessageLite) value;
                }
                Type message = extension.defaultValue.newMessageForType();
                this.extensions.setField(extension.descriptor, message);
                return message;
            }
        }

        public final <Type> MessageType setExtension(GeneratedExtension<MessageType, Type> extension, Type value) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.setField(extension.descriptor, extension.toFieldSetType(value));
            return this;
        }

        public final <Type> MessageType setExtension(GeneratedExtension<MessageType, List<Type>> extension, int index, Type value) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.setRepeatedField(extension.descriptor, index, extension.singularToFieldSetType(value));
            return this;
        }

        public final <Type> MessageType addExtension(GeneratedExtension<MessageType, List<Type>> extension, Type value) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(value));
            return this;
        }

        public final <Type> MessageType clearExtension(GeneratedExtension<MessageType, ?> extension) {
            assertMutable();
            verifyExtensionContainingType(extension);
            ensureExtensionsIsMutable();
            this.extensions.clearField(extension.descriptor);
            return this;
        }

        /* access modifiers changed from: protected */
        public boolean extensionsAreInitialized() {
            return this.extensions.isInitialized();
        }

        /* access modifiers changed from: protected */
        public boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
            ensureExtensionsIsMutable();
            return GeneratedMutableMessageLite.parseUnknownField(this.extensions, getDefaultInstanceForType(), input, unknownFieldsCodedOutput, extensionRegistry, tag);
        }

        public MessageLite immutableCopy() {
            ExtendableBuilder builder = (ExtendableBuilder) GeneratedMutableMessageLite.internalCopyToBuilder(this, internalImmutableDefault());
            builder.internalSetExtensionSet(this.extensions.cloneWithAllFieldsToImmutable());
            return builder.buildPartial();
        }

        /* access modifiers changed from: protected */
        public ExtensionWriter newExtensionWriter() {
            return new ExtensionWriter(false);
        }

        /* access modifiers changed from: protected */
        public ExtensionWriter newMessageSetExtensionWriter() {
            return new ExtensionWriter(true);
        }

        /* access modifiers changed from: protected */
        public int extensionsSerializedSize() {
            return this.extensions.getSerializedSize();
        }

        /* access modifiers changed from: protected */
        public int extensionsSerializedSizeAsMessageSet() {
            return this.extensions.getMessageSetSerializedSize();
        }

        /* access modifiers changed from: protected|final */
        public final void mergeExtensionFields(MessageType other) {
            ensureExtensionsIsMutable();
            this.extensions.mergeFrom(other.extensions);
        }
    }

    public abstract MessageType getDefaultInstanceForType();

    public abstract MessageLite internalImmutableDefault();

    public abstract MessageType mergeFrom(MessageType messageType);

    public Parser<MessageType> getParserForType() {
        throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
    }

    public MessageType clear() {
        assertMutable();
        this.unknownFields = ByteString.EMPTY;
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean parseUnknownField(CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
        return input.skipField(tag, unknownFieldsCodedOutput);
    }

    static Builder internalCopyToBuilder(MutableMessageLite fromMessage, MessageLite toMessagePrototype) {
        Builder builder = toMessagePrototype.newBuilderForType();
        try {
            builder.mergeFrom(fromMessage.toByteArray());
            return builder;
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Failed to parse serialized bytes (should not happen)");
        }
    }

    protected static MessageLite internalImmutableDefault(String name) {
        try {
            return (MessageLite) GeneratedMessageLite.invokeOrDie(GeneratedMessageLite.getMethodOrDie(Class.forName(name), "getDefaultInstance", new Class[0]), null, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Cannot load the corresponding immutable class. Please add necessary dependencies.");
        }
    }

    public MessageLite immutableCopy() {
        MessageLite immutableDefaultInstance = internalImmutableDefault();
        return this == getDefaultInstanceForType() ? immutableDefaultInstance : internalCopyToBuilder(this, immutableDefaultInstance).buildPartial();
    }

    static <MessageType extends MutableMessageLite> boolean parseUnknownField(FieldSet<ExtensionDescriptor> extensions, MessageType defaultInstance, CodedInputStream input, CodedOutputStream unknownFieldsCodedOutput, ExtensionRegistryLite extensionRegistry, int tag) throws IOException {
        int wireType = WireFormat.getTagWireType(tag);
        GeneratedExtension<MessageType, ?> extension = extensionRegistry.findLiteExtensionByNumber(defaultInstance, WireFormat.getTagFieldNumber(tag));
        boolean unknown = false;
        boolean packed = false;
        if (extension == null) {
            unknown = true;
        } else if (wireType == FieldSet.getWireFormatForFieldType(extension.descriptor.getLiteType(), false)) {
            packed = false;
        } else if (extension.descriptor.isRepeated && extension.descriptor.type.isPackable() && wireType == FieldSet.getWireFormatForFieldType(extension.descriptor.getLiteType(), true)) {
            packed = true;
        } else {
            unknown = true;
        }
        if (unknown) {
            return input.skipField(tag, unknownFieldsCodedOutput);
        }
        if (packed) {
            int limit = input.pushLimit(input.readRawVarint32());
            if (extension.descriptor.getLiteType() == FieldType.ENUM) {
                while (input.getBytesUntilLimit() > 0) {
                    EnumLite value = extension.descriptor.getEnumType().findValueByNumber(input.readEnum());
                    if (value == null) {
                        return true;
                    }
                    extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(value));
                }
            } else {
                while (input.getBytesUntilLimit() > 0) {
                    extensions.addRepeatedField(extension.descriptor, FieldSet.readPrimitiveFieldForMutable(input, extension.descriptor.getLiteType(), false));
                }
            }
            input.popLimit(limit);
        } else {
            Object value2;
            switch (extension.descriptor.getLiteJavaType()) {
                case MESSAGE:
                    MutableMessageLite message = ((MutableMessageLite) extension.messageDefaultInstance).newMessageForType();
                    if (extension.descriptor.getLiteType() == FieldType.GROUP) {
                        input.readGroup(extension.getNumber(), message, extensionRegistry);
                    } else {
                        input.readMessage(message, extensionRegistry);
                    }
                    MutableMessageLite value22 = message;
                    break;
                case ENUM:
                    int rawValue = input.readEnum();
                    value22 = extension.descriptor.getEnumType().findValueByNumber(rawValue);
                    if (value22 == null) {
                        unknownFieldsCodedOutput.writeRawVarint32(tag);
                        unknownFieldsCodedOutput.writeUInt32NoTag(rawValue);
                        return true;
                    }
                    break;
                default:
                    value22 = FieldSet.readPrimitiveFieldForMutable(input, extension.descriptor.getLiteType(), false);
                    break;
            }
            if (extension.descriptor.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, extension.singularToFieldSetType(value22));
            } else {
                extensions.setField(extension.descriptor, extension.singularToFieldSetType(value22));
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public Object writeReplace() throws ObjectStreamException {
        return new SerializedForm(this);
    }
}
