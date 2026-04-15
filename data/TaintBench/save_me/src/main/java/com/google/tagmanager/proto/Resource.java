package com.google.tagmanager.proto;

import com.google.tagmanager.protobuf.AbstractParser;
import com.google.tagmanager.protobuf.ByteString;
import com.google.tagmanager.protobuf.CodedInputStream;
import com.google.tagmanager.protobuf.CodedOutputStream;
import com.google.tagmanager.protobuf.ExtensionRegistryLite;
import com.google.tagmanager.protobuf.GeneratedMessageLite;
import com.google.tagmanager.protobuf.Internal;
import com.google.tagmanager.protobuf.InvalidProtocolBufferException;
import com.google.tagmanager.protobuf.MessageLiteOrBuilder;
import com.google.tagmanager.protobuf.MutableMessageLite;
import com.google.tagmanager.protobuf.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;

public final class Resource {

    public interface ResourceWithMetadataOrBuilder extends MessageLiteOrBuilder {
        com.google.analytics.containertag.proto.Serving.Resource getResource();

        long getTimeStamp();

        boolean hasResource();

        boolean hasTimeStamp();
    }

    public static final class ResourceWithMetadata extends GeneratedMessageLite implements ResourceWithMetadataOrBuilder {
        public static Parser<ResourceWithMetadata> PARSER = new AbstractParser<ResourceWithMetadata>() {
            public ResourceWithMetadata parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new ResourceWithMetadata(input, extensionRegistry);
            }
        };
        public static final int RESOURCE_FIELD_NUMBER = 2;
        public static final int TIME_STAMP_FIELD_NUMBER = 1;
        private static final ResourceWithMetadata defaultInstance = new ResourceWithMetadata(true);
        private static volatile MutableMessageLite mutableDefault = null;
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public com.google.analytics.containertag.proto.Serving.Resource resource_;
        /* access modifiers changed from: private */
        public long timeStamp_;
        /* access modifiers changed from: private|final */
        public final ByteString unknownFields;

        public static final class Builder extends com.google.tagmanager.protobuf.GeneratedMessageLite.Builder<ResourceWithMetadata, Builder> implements ResourceWithMetadataOrBuilder {
            private int bitField0_;
            private com.google.analytics.containertag.proto.Serving.Resource resource_ = com.google.analytics.containertag.proto.Serving.Resource.getDefaultInstance();
            private long timeStamp_;

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
            }

            /* access modifiers changed from: private|static */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.timeStamp_ = 0;
                this.bitField0_ &= -2;
                this.resource_ = com.google.analytics.containertag.proto.Serving.Resource.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public ResourceWithMetadata getDefaultInstanceForType() {
                return ResourceWithMetadata.getDefaultInstance();
            }

            public ResourceWithMetadata build() {
                ResourceWithMetadata result = buildPartial();
                if (result.isInitialized()) {
                    return result;
                }
                throw com.google.tagmanager.protobuf.AbstractMessageLite.Builder.newUninitializedMessageException(result);
            }

            public ResourceWithMetadata buildPartial() {
                ResourceWithMetadata result = new ResourceWithMetadata((com.google.tagmanager.protobuf.GeneratedMessageLite.Builder) this);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.timeStamp_ = this.timeStamp_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.resource_ = this.resource_;
                result.bitField0_ = to_bitField0_;
                return result;
            }

            public Builder mergeFrom(ResourceWithMetadata other) {
                if (other != ResourceWithMetadata.getDefaultInstance()) {
                    if (other.hasTimeStamp()) {
                        setTimeStamp(other.getTimeStamp());
                    }
                    if (other.hasResource()) {
                        mergeResource(other.getResource());
                    }
                    setUnknownFields(getUnknownFields().concat(other.unknownFields));
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasTimeStamp() && hasResource() && getResource().isInitialized()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ResourceWithMetadata parsedMessage = null;
                try {
                    parsedMessage = (ResourceWithMetadata) ResourceWithMetadata.PARSER.parsePartialFrom(input, extensionRegistry);
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    return this;
                } catch (InvalidProtocolBufferException e) {
                    parsedMessage = (ResourceWithMetadata) e.getUnfinishedMessage();
                    throw e;
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
            }

            public boolean hasTimeStamp() {
                return (this.bitField0_ & 1) == 1;
            }

            public long getTimeStamp() {
                return this.timeStamp_;
            }

            public Builder setTimeStamp(long value) {
                this.bitField0_ |= 1;
                this.timeStamp_ = value;
                return this;
            }

            public Builder clearTimeStamp() {
                this.bitField0_ &= -2;
                this.timeStamp_ = 0;
                return this;
            }

            public boolean hasResource() {
                return (this.bitField0_ & 2) == 2;
            }

            public com.google.analytics.containertag.proto.Serving.Resource getResource() {
                return this.resource_;
            }

            public Builder setResource(com.google.analytics.containertag.proto.Serving.Resource value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.resource_ = value;
                this.bitField0_ |= 2;
                return this;
            }

            public Builder setResource(com.google.analytics.containertag.proto.Serving.Resource.Builder builderForValue) {
                this.resource_ = builderForValue.build();
                this.bitField0_ |= 2;
                return this;
            }

            public Builder mergeResource(com.google.analytics.containertag.proto.Serving.Resource value) {
                if ((this.bitField0_ & 2) != 2 || this.resource_ == com.google.analytics.containertag.proto.Serving.Resource.getDefaultInstance()) {
                    this.resource_ = value;
                } else {
                    this.resource_ = com.google.analytics.containertag.proto.Serving.Resource.newBuilder(this.resource_).mergeFrom(value).buildPartial();
                }
                this.bitField0_ |= 2;
                return this;
            }

            public Builder clearResource() {
                this.resource_ = com.google.analytics.containertag.proto.Serving.Resource.getDefaultInstance();
                this.bitField0_ &= -3;
                return this;
            }
        }

        private ResourceWithMetadata(com.google.tagmanager.protobuf.GeneratedMessageLite.Builder builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private ResourceWithMetadata(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = ByteString.EMPTY;
        }

        public static ResourceWithMetadata getDefaultInstance() {
            return defaultInstance;
        }

        public ResourceWithMetadata getDefaultInstanceForType() {
            return defaultInstance;
        }

        private ResourceWithMetadata(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
            OutputStream unknownFieldsOutput = ByteString.newOutput();
            CodedOutputStream unknownFieldsCodedOutput = CodedOutputStream.newInstance(unknownFieldsOutput);
            boolean done = false;
            while (!done) {
                try {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        case 8:
                            this.bitField0_ |= 1;
                            this.timeStamp_ = input.readInt64();
                            break;
                        case 18:
                            com.google.analytics.containertag.proto.Serving.Resource.Builder subBuilder = null;
                            if ((this.bitField0_ & 2) == 2) {
                                subBuilder = this.resource_.toBuilder();
                            }
                            this.resource_ = (com.google.analytics.containertag.proto.Serving.Resource) input.readMessage(com.google.analytics.containertag.proto.Serving.Resource.PARSER, extensionRegistry);
                            if (subBuilder != null) {
                                subBuilder.mergeFrom(this.resource_);
                                this.resource_ = subBuilder.buildPartial();
                            }
                            this.bitField0_ |= 2;
                            break;
                        default:
                            if (!parseUnknownField(input, unknownFieldsCodedOutput, extensionRegistry, tag)) {
                                done = true;
                                break;
                            }
                            break;
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw e.setUnfinishedMessage(this);
                } catch (IOException e2) {
                    throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                } catch (Throwable th) {
                    try {
                        unknownFieldsCodedOutput.flush();
                    } catch (IOException e3) {
                        makeExtensionsImmutable();
                    } finally {
                        this.unknownFields = unknownFieldsOutput.toByteString();
                    }
                    makeExtensionsImmutable();
                }
            }
            try {
                unknownFieldsCodedOutput.flush();
            } catch (IOException e4) {
            } finally {
                this.unknownFields = unknownFieldsOutput.toByteString();
            }
            makeExtensionsImmutable();
        }

        static {
            defaultInstance.initFields();
        }

        public Parser<ResourceWithMetadata> getParserForType() {
            return PARSER;
        }

        public boolean hasTimeStamp() {
            return (this.bitField0_ & 1) == 1;
        }

        public long getTimeStamp() {
            return this.timeStamp_;
        }

        public boolean hasResource() {
            return (this.bitField0_ & 2) == 2;
        }

        public com.google.analytics.containertag.proto.Serving.Resource getResource() {
            return this.resource_;
        }

        private void initFields() {
            this.timeStamp_ = 0;
            this.resource_ = com.google.analytics.containertag.proto.Serving.Resource.getDefaultInstance();
        }

        public final boolean isInitialized() {
            boolean z = true;
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != (byte) -1) {
                if (isInitialized != (byte) 1) {
                    z = false;
                }
                return z;
            } else if (!hasTimeStamp()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            } else if (!hasResource()) {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            } else if (getResource().isInitialized()) {
                this.memoizedIsInitialized = (byte) 1;
                return true;
            } else {
                this.memoizedIsInitialized = (byte) 0;
                return false;
            }
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeInt64(1, this.timeStamp_);
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeMessage(2, this.resource_);
            }
            output.writeRawBytes(this.unknownFields);
        }

        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            size = 0;
            if ((this.bitField0_ & 1) == 1) {
                size = 0 + CodedOutputStream.computeInt64Size(1, this.timeStamp_);
            }
            if ((this.bitField0_ & 2) == 2) {
                size += CodedOutputStream.computeMessageSize(2, this.resource_);
            }
            size += this.unknownFields.size();
            this.memoizedSerializedSize = size;
            return size;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ResourceWithMetadata)) {
                return super.equals(obj);
            }
            ResourceWithMetadata other = (ResourceWithMetadata) obj;
            boolean result = true && hasTimeStamp() == other.hasTimeStamp();
            if (hasTimeStamp()) {
                result = result && getTimeStamp() == other.getTimeStamp();
            }
            if (result && hasResource() == other.hasResource()) {
                result = true;
            } else {
                result = false;
            }
            if (hasResource()) {
                if (result && getResource().equals(other.getResource())) {
                    result = true;
                } else {
                    result = false;
                }
            }
            return result;
        }

        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = ResourceWithMetadata.class.hashCode() + 779;
            if (hasTimeStamp()) {
                hash = (((hash * 37) + 1) * 53) + Internal.hashLong(getTimeStamp());
            }
            if (hasResource()) {
                hash = (((hash * 37) + 2) * 53) + getResource().hashCode();
            }
            hash = (hash * 29) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash;
            return hash;
        }

        /* access modifiers changed from: protected */
        public MutableMessageLite internalMutableDefault() {
            if (mutableDefault == null) {
                mutableDefault = GeneratedMessageLite.internalMutableDefault("com.google.tagmanager.proto.MutableResource$ResourceWithMetadata");
            }
            return mutableDefault;
        }

        public static ResourceWithMetadata parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return (ResourceWithMetadata) PARSER.parseFrom(data);
        }

        public static ResourceWithMetadata parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (ResourceWithMetadata) PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResourceWithMetadata parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return (ResourceWithMetadata) PARSER.parseFrom(data);
        }

        public static ResourceWithMetadata parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (ResourceWithMetadata) PARSER.parseFrom(data, extensionRegistry);
        }

        public static ResourceWithMetadata parseFrom(InputStream input) throws IOException {
            return (ResourceWithMetadata) PARSER.parseFrom(input);
        }

        public static ResourceWithMetadata parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (ResourceWithMetadata) PARSER.parseFrom(input, extensionRegistry);
        }

        public static ResourceWithMetadata parseDelimitedFrom(InputStream input) throws IOException {
            return (ResourceWithMetadata) PARSER.parseDelimitedFrom(input);
        }

        public static ResourceWithMetadata parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (ResourceWithMetadata) PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static ResourceWithMetadata parseFrom(CodedInputStream input) throws IOException {
            return (ResourceWithMetadata) PARSER.parseFrom(input);
        }

        public static ResourceWithMetadata parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (ResourceWithMetadata) PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(ResourceWithMetadata prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }
    }

    private Resource() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }
}
