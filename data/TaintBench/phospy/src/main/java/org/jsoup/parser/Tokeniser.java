package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

class Tokeniser {
    static final char replacementChar = '�';
    private StringBuilder charBuffer = new StringBuilder();
    Comment commentPending;
    StringBuilder dataBuffer;
    Doctype doctypePending;
    private Token emitPending;
    private ParseErrorList errors;
    private boolean isEmitPending = false;
    private StartTag lastStartTag;
    private CharacterReader reader;
    private boolean selfClosingFlagAcknowledged = true;
    private TokeniserState state = TokeniserState.Data;
    Tag tagPending;

    Tokeniser(CharacterReader reader, ParseErrorList errors) {
        this.reader = reader;
        this.errors = errors;
    }

    /* access modifiers changed from: 0000 */
    public Token read() {
        if (!this.selfClosingFlagAcknowledged) {
            error("Self closing flag not acknowledged");
            this.selfClosingFlagAcknowledged = true;
        }
        while (!this.isEmitPending) {
            this.state.read(this, this.reader);
        }
        if (this.charBuffer.length() > 0) {
            String str = this.charBuffer.toString();
            this.charBuffer.delete(0, this.charBuffer.length());
            return new Character(str);
        }
        this.isEmitPending = false;
        return this.emitPending;
    }

    /* access modifiers changed from: 0000 */
    public void emit(Token token) {
        Validate.isFalse(this.isEmitPending, "There is an unread token pending!");
        this.emitPending = token;
        this.isEmitPending = true;
        if (token.type == TokenType.StartTag) {
            StartTag startTag = (StartTag) token;
            this.lastStartTag = startTag;
            if (startTag.selfClosing) {
                this.selfClosingFlagAcknowledged = false;
            }
        } else if (token.type == TokenType.EndTag && ((EndTag) token).attributes != null) {
            error("Attributes incorrectly present on end tag");
        }
    }

    /* access modifiers changed from: 0000 */
    public void emit(String str) {
        this.charBuffer.append(str);
    }

    /* access modifiers changed from: 0000 */
    public void emit(char[] chars) {
        this.charBuffer.append(chars);
    }

    /* access modifiers changed from: 0000 */
    public void emit(char c) {
        this.charBuffer.append(c);
    }

    /* access modifiers changed from: 0000 */
    public TokeniserState getState() {
        return this.state;
    }

    /* access modifiers changed from: 0000 */
    public void transition(TokeniserState state) {
        this.state = state;
    }

    /* access modifiers changed from: 0000 */
    public void advanceTransition(TokeniserState state) {
        this.reader.advance();
        this.state = state;
    }

    /* access modifiers changed from: 0000 */
    public void acknowledgeSelfClosingFlag() {
        this.selfClosingFlagAcknowledged = true;
    }

    /* access modifiers changed from: 0000 */
    public char[] consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) {
        if (this.reader.isEmpty()) {
            return null;
        }
        if ((additionalAllowedCharacter != null && additionalAllowedCharacter.charValue() == this.reader.current()) || this.reader.matchesAny(9, 10, 13, 12, ' ', '<', '&')) {
            return null;
        }
        this.reader.mark();
        if (this.reader.matchConsume("#")) {
            boolean isHexMode = this.reader.matchConsumeIgnoreCase("X");
            String numRef = isHexMode ? this.reader.consumeHexSequence() : this.reader.consumeDigitSequence();
            if (numRef.length() == 0) {
                characterReferenceError("numeric reference with no numerals");
                this.reader.rewindToMark();
                return null;
            }
            if (!this.reader.matchConsume(";")) {
                characterReferenceError("missing semicolon");
            }
            int charval = -1;
            try {
                charval = Integer.valueOf(numRef, isHexMode ? 16 : 10).intValue();
            } catch (NumberFormatException e) {
            }
            if (charval != -1 && ((charval < 55296 || charval > 57343) && charval <= 1114111)) {
                return Character.toChars(charval);
            }
            characterReferenceError("character outside of valid range");
            return new char[]{replacementChar};
        }
        boolean found;
        String nameRef = this.reader.consumeLetterThenDigitSequence();
        boolean looksLegit = this.reader.matches(';');
        if (Entities.isBaseNamedEntity(nameRef) || (Entities.isNamedEntity(nameRef) && looksLegit)) {
            found = true;
        } else {
            found = false;
        }
        if (!found) {
            this.reader.rewindToMark();
            if (!looksLegit) {
                return null;
            }
            characterReferenceError(String.format("invalid named referenece '%s'", new Object[]{nameRef}));
            return null;
        } else if (inAttribute && (this.reader.matchesLetter() || this.reader.matchesDigit() || this.reader.matchesAny('=', '-', '_'))) {
            this.reader.rewindToMark();
            return null;
        } else {
            if (!this.reader.matchConsume(";")) {
                characterReferenceError("missing semicolon");
            }
            return new char[]{Entities.getCharacterByName(nameRef).charValue()};
        }
    }

    /* access modifiers changed from: 0000 */
    public Tag createTagPending(boolean start) {
        this.tagPending = start ? new StartTag() : new EndTag();
        return this.tagPending;
    }

    /* access modifiers changed from: 0000 */
    public void emitTagPending() {
        this.tagPending.finaliseTag();
        emit(this.tagPending);
    }

    /* access modifiers changed from: 0000 */
    public void createCommentPending() {
        this.commentPending = new Comment();
    }

    /* access modifiers changed from: 0000 */
    public void emitCommentPending() {
        emit(this.commentPending);
    }

    /* access modifiers changed from: 0000 */
    public void createDoctypePending() {
        this.doctypePending = new Doctype();
    }

    /* access modifiers changed from: 0000 */
    public void emitDoctypePending() {
        emit(this.doctypePending);
    }

    /* access modifiers changed from: 0000 */
    public void createTempBuffer() {
        this.dataBuffer = new StringBuilder();
    }

    /* access modifiers changed from: 0000 */
    public boolean isAppropriateEndTagToken() {
        if (this.lastStartTag == null) {
            return false;
        }
        return this.tagPending.tagName.equals(this.lastStartTag.tagName);
    }

    /* access modifiers changed from: 0000 */
    public String appropriateEndTagName() {
        return this.lastStartTag.tagName;
    }

    /* access modifiers changed from: 0000 */
    public void error(TokeniserState state) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Unexpected character '%s' in input state [%s]", Character.valueOf(this.reader.current()), state));
        }
    }

    /* access modifiers changed from: 0000 */
    public void eofError(TokeniserState state) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Unexpectedly reached end of file (EOF) in input state [%s]", state));
        }
    }

    private void characterReferenceError(String message) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Invalid character reference: %s", message));
        }
    }

    private void error(String errorMsg) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), errorMsg));
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean currentNodeInHtmlNS() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public String unescapeEntities(boolean inAttribute) {
        StringBuilder builder = new StringBuilder();
        while (!this.reader.isEmpty()) {
            builder.append(this.reader.consumeTo('&'));
            if (this.reader.matches('&')) {
                this.reader.consume();
                char[] c = consumeCharacterReference(null, inAttribute);
                if (c == null || c.length == 0) {
                    builder.append('&');
                } else {
                    builder.append(c);
                }
            }
        }
        return builder.toString();
    }
}
