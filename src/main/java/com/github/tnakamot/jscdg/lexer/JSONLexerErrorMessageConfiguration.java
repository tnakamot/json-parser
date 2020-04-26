package com.github.tnakamot.jscdg.lexer;

public class JSONLexerErrorMessageConfiguration {
    private final boolean showFullPath;
    private final boolean showLineAndColumnNumber;
    private final boolean showErrorLine;

    private JSONLexerErrorMessageConfiguration (
            boolean showFullPath,
            boolean showLineAndColumnNumber,
            boolean showErrorLine
    ) {
        this.showFullPath = showFullPath;
        this.showLineAndColumnNumber = showLineAndColumnNumber;
        this.showErrorLine = showErrorLine;
    }

    public boolean showFullPath() {
        return showFullPath;
    }

    public boolean showLineAndColumnNumber() {
        return showLineAndColumnNumber;
    }

    public boolean showErrorLine() {
        return showErrorLine;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean showFullPath            = false;
        private boolean showLineAndColumnNumber = false;
        private boolean showErrorLine           = false;

        public Builder setShowFullPath(boolean b) {
            this.showFullPath = b;
            return this;
        }

        public Builder setShowLineAndColumnNumber(boolean b) {
            this.showLineAndColumnNumber = b;
            return this;
        }

        public Builder setShowErrorLine(boolean b) {
            this.showErrorLine = b;
            return this;
        }

        public JSONLexerErrorMessageConfiguration build() {
            return new JSONLexerErrorMessageConfiguration(
                    showFullPath,
                    showLineAndColumnNumber,
                    showErrorLine
            );
        }
    }
}
