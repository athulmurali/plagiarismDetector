package com.blacksheep;

import com.blacksheep.parser.Python3Lexer;
import com.blacksheep.parser.Python3Parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * This class contains the implementation for creating AST using ANTLR
 */
public class ParserFacade {

    /**
     * Converts the input file into a string
     *
     * @param file : file path
     * @param encoding : encoding type for the string
     */
    private String readFile(File file, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, encoding);
    }

    /**
     * Converts the input file into a tokens and returns the Input context of the source code AST
     *
     * @param file : file path
     */
    public Python3Parser.File_inputContext parse(File file) throws IOException {
        String code = readFile(file, Charset.forName("UTF-8"));
        Python3Lexer lexer = new Python3Lexer(new ANTLRInputStream(code));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        Python3Parser parser = new Python3Parser(tokens);

        return parser.file_input();
    }
}
