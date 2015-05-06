package gens.com.vasinn;

import com.github.devnied.emvnfccard.parser.IProvider;

public class YourProvider implements IProvider {

    @Override
    public byte[] transceive(final byte[] pCommand) {
        // implement this
        return pCommand;
    }
}