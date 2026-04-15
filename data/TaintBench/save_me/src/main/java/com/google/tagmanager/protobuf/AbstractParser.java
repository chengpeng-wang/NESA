package com.google.tagmanager.protobuf;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractParser<MessageType extends MessageLite> implements Parser<MessageType> {
    private static final ExtensionRegistryLite EMPTY_REGISTRY = ExtensionRegistryLite.getEmptyRegistry();

    private UninitializedMessageException newUninitializedMessageException(MessageType message) {
        if (message instanceof AbstractMessageLite) {
            return ((AbstractMessageLite) message).newUninitializedMessageException();
        }
        if (message instanceof AbstractMutableMessageLite) {
            return ((AbstractMutableMessageLite) message).newUninitializedMessageException();
        }
        return new UninitializedMessageException((MessageLite) message);
    }

    private MessageType checkMessageInitialized(MessageType message) throws InvalidProtocolBufferException {
        if (message == null || message.isInitialized()) {
            return message;
        }
        throw newUninitializedMessageException(message).asInvalidProtocolBufferException().setUnfinishedMessage(message);
    }

    public MessageType parsePartialFrom(CodedInputStream input) throws InvalidProtocolBufferException {
        return (MessageLite) parsePartialFrom(input, EMPTY_REGISTRY);
    }

    public MessageType parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized((MessageLite) parsePartialFrom(input, extensionRegistry));
    }

    public MessageType parseFrom(CodedInputStream input) throws InvalidProtocolBufferException {
        return parseFrom(input, EMPTY_REGISTRY);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0017 A:{ExcHandler: IOException (r0_0 'e' java.io.IOException), Splitter:B:0:0x0000} */
    /* JADX WARNING: Missing block: B:12:0x0017, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:14:0x001f, code skipped:
            throw new java.lang.RuntimeException("Reading from a ByteString threw an IOException (should never happen).", r0);
     */
    public MessageType parsePartialFrom(com.google.tagmanager.protobuf.ByteString r6, com.google.tagmanager.protobuf.ExtensionRegistryLite r7) throws com.google.tagmanager.protobuf.InvalidProtocolBufferException {
        /*
        r5 = this;
        r1 = r6.newCodedInput();	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
        r2 = r5.parsePartialFrom(r1, r7);	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
        r2 = (com.google.tagmanager.protobuf.MessageLite) r2;	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
        r3 = 0;
        r1.checkLastTagWas(r3);	 Catch:{ InvalidProtocolBufferException -> 0x000f, IOException -> 0x0017 }
        return r2;
    L_0x000f:
        r0 = move-exception;
        r3 = r0.setUnfinishedMessage(r2);	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
        throw r3;	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
    L_0x0015:
        r0 = move-exception;
        throw r0;
    L_0x0017:
        r0 = move-exception;
        r3 = new java.lang.RuntimeException;
        r4 = "Reading from a ByteString threw an IOException (should never happen).";
        r3.<init>(r4, r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.protobuf.AbstractParser.parsePartialFrom(com.google.tagmanager.protobuf.ByteString, com.google.tagmanager.protobuf.ExtensionRegistryLite):com.google.tagmanager.protobuf.MessageLite");
    }

    public MessageType parsePartialFrom(ByteString data) throws InvalidProtocolBufferException {
        return parsePartialFrom(data, EMPTY_REGISTRY);
    }

    public MessageType parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(parsePartialFrom(data, extensionRegistry));
    }

    public MessageType parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return parseFrom(data, EMPTY_REGISTRY);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0017 A:{ExcHandler: IOException (r0_0 'e' java.io.IOException), Splitter:B:0:0x0000} */
    /* JADX WARNING: Missing block: B:12:0x0017, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:14:0x001f, code skipped:
            throw new java.lang.RuntimeException("Reading from a byte array threw an IOException (should never happen).", r0);
     */
    public MessageType parsePartialFrom(byte[] r6, int r7, int r8, com.google.tagmanager.protobuf.ExtensionRegistryLite r9) throws com.google.tagmanager.protobuf.InvalidProtocolBufferException {
        /*
        r5 = this;
        r1 = com.google.tagmanager.protobuf.CodedInputStream.newInstance(r6, r7, r8);	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
        r2 = r5.parsePartialFrom(r1, r9);	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
        r2 = (com.google.tagmanager.protobuf.MessageLite) r2;	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
        r3 = 0;
        r1.checkLastTagWas(r3);	 Catch:{ InvalidProtocolBufferException -> 0x000f, IOException -> 0x0017 }
        return r2;
    L_0x000f:
        r0 = move-exception;
        r3 = r0.setUnfinishedMessage(r2);	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
        throw r3;	 Catch:{ InvalidProtocolBufferException -> 0x0015, IOException -> 0x0017 }
    L_0x0015:
        r0 = move-exception;
        throw r0;
    L_0x0017:
        r0 = move-exception;
        r3 = new java.lang.RuntimeException;
        r4 = "Reading from a byte array threw an IOException (should never happen).";
        r3.<init>(r4, r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.tagmanager.protobuf.AbstractParser.parsePartialFrom(byte[], int, int, com.google.tagmanager.protobuf.ExtensionRegistryLite):com.google.tagmanager.protobuf.MessageLite");
    }

    public MessageType parsePartialFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
        return parsePartialFrom(data, off, len, EMPTY_REGISTRY);
    }

    public MessageType parsePartialFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return parsePartialFrom(data, 0, data.length, extensionRegistry);
    }

    public MessageType parsePartialFrom(byte[] data) throws InvalidProtocolBufferException {
        return parsePartialFrom(data, 0, data.length, EMPTY_REGISTRY);
    }

    public MessageType parseFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(parsePartialFrom(data, off, len, extensionRegistry));
    }

    public MessageType parseFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
        return parseFrom(data, off, len, EMPTY_REGISTRY);
    }

    public MessageType parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return parseFrom(data, 0, data.length, extensionRegistry);
    }

    public MessageType parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return parseFrom(data, EMPTY_REGISTRY);
    }

    public MessageType parsePartialFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        CodedInputStream codedInput = CodedInputStream.newInstance(input);
        MessageLite message = (MessageLite) parsePartialFrom(codedInput, extensionRegistry);
        try {
            codedInput.checkLastTagWas(0);
            return message;
        } catch (InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(message);
        }
    }

    public MessageType parsePartialFrom(InputStream input) throws InvalidProtocolBufferException {
        return parsePartialFrom(input, EMPTY_REGISTRY);
    }

    public MessageType parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(parsePartialFrom(input, extensionRegistry));
    }

    public MessageType parseFrom(InputStream input) throws InvalidProtocolBufferException {
        return parseFrom(input, EMPTY_REGISTRY);
    }

    public MessageType parsePartialDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        try {
            int firstByte = input.read();
            if (firstByte == -1) {
                return null;
            }
            return parsePartialFrom(new LimitedInputStream(input, CodedInputStream.readRawVarint32(firstByte, input)), extensionRegistry);
        } catch (IOException e) {
            throw new InvalidProtocolBufferException(e.getMessage());
        }
    }

    public MessageType parsePartialDelimitedFrom(InputStream input) throws InvalidProtocolBufferException {
        return parsePartialDelimitedFrom(input, EMPTY_REGISTRY);
    }

    public MessageType parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(parsePartialDelimitedFrom(input, extensionRegistry));
    }

    public MessageType parseDelimitedFrom(InputStream input) throws InvalidProtocolBufferException {
        return parseDelimitedFrom(input, EMPTY_REGISTRY);
    }
}
