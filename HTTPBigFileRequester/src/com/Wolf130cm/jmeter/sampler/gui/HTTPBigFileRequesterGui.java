package com.Wolf130cm.jmeter.sampler.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.gui.JLabeledTextField;

import com.Wolf130cm.jmeter.sampler.HTTPBigFileRequester;

public class HTTPBigFileRequesterGui extends AbstractSamplerGui {

	private static final long serialVersionUID = 7809671022852446503L;

	private JLabeledTextField DA_WebServerHost;
	private JLabeledTextField DA_WebServerPort;
	private JLabeledTextField DA_WebServerPath;
	private JLabeledChoice DA_WebServerProtocol;
	private JLabeledChoice DA_WebServerMethod;
	private JLabeledTextField DA_XAuthToken;
	private JLabeledTextField DA_PUTFile;
	private JLabeledTextField DA_GETFile;
	private JLabeledTextField DA_HTTPSendCache;
	private JCheckBox DA_Abort;
	private JLabeledTextField DA_AbortSize;

	public HTTPBigFileRequesterGui() {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());
		add(makeTitlePanel(), BorderLayout.NORTH);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(createHttpRequestPanel());
		mainPanel.add(samplerSettingPanel());
		add(mainPanel, BorderLayout.CENTER);
	}


	private Component createHttpRequestPanel() {
		JPanel dataPanel = new VerticalPanel();
		dataPanel.setBorder(BorderFactory.createTitledBorder("BF HTTP Request"));

		Box verticalBox = Box.createVerticalBox();
		dataPanel.add(verticalBox, BorderLayout.SOUTH);

		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);

		DA_WebServerProtocol = new JLabeledChoice();
		FlowLayout flowLayout = (FlowLayout) DA_WebServerProtocol.getLayout();
		flowLayout.setVgap(0);
		DA_WebServerProtocol.setLabel("Protocol: ");
		DA_WebServerProtocol.setValues(new String[] { HTTPConstants.PROTOCOL_HTTP });
		horizontalBox_1.add(DA_WebServerProtocol);

		horizontalBox_1.add(Box.createRigidArea(new Dimension(5, 1)));

		DA_WebServerHost = new JLabeledTextField("ServerIP or HostName(Req): ");
		horizontalBox_1.add(DA_WebServerHost);

		horizontalBox_1.add(Box.createRigidArea(new Dimension(5, 1)));

		DA_WebServerPort = new JLabeledTextField("PortNo.(Req): ");
		horizontalBox_1.add(DA_WebServerPort);

		horizontalBox_1.add(Box.createRigidArea(new Dimension(5, 1)));

		DA_WebServerMethod = new JLabeledChoice();
		horizontalBox_1.add(DA_WebServerMethod);
		flowLayout = (FlowLayout) DA_WebServerMethod.getLayout();
		flowLayout.setVgap(0);
		DA_WebServerMethod.setLabel("method: ");
		DA_WebServerMethod.setValues(new String[] { HTTPConstants.PUT, HTTPConstants.GET, HTTPConstants.DELETE });
		DA_WebServerMethod.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (HTTPConstants.PUT.equals(DA_WebServerMethod.getText())) {
					DA_PUTFile.setEnabled(true);
				} else {
					DA_PUTFile.setEnabled(false);
				}
				if (HTTPConstants.GET.equals(DA_WebServerMethod.getText())) {
					DA_GETFile.setEnabled(true);
				} else {
					DA_GETFile.setEnabled(false);
				}
			}
		});

		verticalBox.add(Box.createRigidArea(new Dimension(1, 5)));

		DA_WebServerPath = new JLabeledTextField("Path(Req): ");
		verticalBox.add(DA_WebServerPath);

		verticalBox.add(Box.createRigidArea(new Dimension(1, 5)));

		DA_XAuthToken = new JLabeledTextField("X-Auth-Token(Req): ");
		verticalBox.add(DA_XAuthToken);

		verticalBox.add(Box.createRigidArea(new Dimension(1, 5)));

		DA_PUTFile = new JLabeledTextField("PUT file path(Req): ");
		verticalBox.add(DA_PUTFile);

		verticalBox.add(Box.createRigidArea(new Dimension(1, 5)));

		DA_GETFile = new JLabeledTextField("GET file save path(Opt): ");
		verticalBox.add(DA_GETFile);
		DA_GETFile.setEnabled(false);

		return dataPanel;
	}

	private Component samplerSettingPanel() {

		JPanel dataPanel = new VerticalPanel();
		dataPanel.setBorder(BorderFactory.createTitledBorder("HTTP Request Setting"));

		Box VBox = Box.createVerticalBox();
		dataPanel.add(VBox, BorderLayout.SOUTH);

		DA_HTTPSendCache = new JLabeledTextField("Stream cache(MB)(Default 2MB): ");
		VBox.add(DA_HTTPSendCache);

		VBox.add(Box.createRigidArea(new Dimension(1, 5)));

		Box HBox = Box.createHorizontalBox();
		VBox.add(HBox);

		DA_Abort = new JCheckBox("Abort");
		DA_Abort.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent paramItemEvent) {
				if (paramItemEvent.getStateChange() == ItemEvent.SELECTED) {
					DA_AbortSize.setEnabled(true);
			    } else {
					DA_AbortSize.setEnabled(false);
			    }
			}
		});
		HBox.add(DA_Abort);

		HBox.add(Box.createRigidArea(new Dimension(5, 1)));

		DA_AbortSize = new JLabeledTextField("Timing(MB)(Default 50MB): ");
		HBox.add(DA_AbortSize);
		DA_AbortSize.setEnabled(false);

		return dataPanel;
	}

	@Override
	public String getLabelResource() {
		return null;
	}

	public String getStaticLabel() {
		return "HTTP Big File Requester";
	}

	@Override
	public TestElement createTestElement() {
		HTTPBigFileRequester sampler = new HTTPBigFileRequester();
		modifyTestElement(sampler);
		return sampler;
	}

	@Override
	public void modifyTestElement(TestElement testElement) {
		testElement.clear();
		configureTestElement(testElement);

		testElement.setProperty("DA_WebServerHost", DA_WebServerHost.getText());
		testElement.setProperty("DA_WebServerPort", DA_WebServerPort.getText());

		testElement.setProperty("DA_WebServerPath", DA_WebServerPath.getText());
		testElement.setProperty("DA_WebServerProtocol", DA_WebServerProtocol.getText());
		testElement.setProperty("DA_WebServerMethod", DA_WebServerMethod.getText());
		testElement.setProperty("DA_XAuthToken", DA_XAuthToken.getText());
		testElement.setProperty("DA_PUTFile", DA_PUTFile.getText());
		testElement.setProperty("DA_GETFile", DA_GETFile.getText());
		testElement.setProperty("DA_HTTPSendCache", DA_HTTPSendCache.getText());
		testElement.setProperty("DA_Abort", DA_Abort.isSelected());
		testElement.setProperty("DA_AbortSize", DA_AbortSize.getText());
	}

	public void configure(TestElement testElement) {
		DA_WebServerHost.setText(testElement.getPropertyAsString("DA_WebServerHost"));
		DA_WebServerPort.setText(testElement.getPropertyAsString("DA_WebServerPort"));

		DA_WebServerPath.setText(testElement.getPropertyAsString("DA_WebServerPath"));
		DA_WebServerProtocol.setText(testElement.getPropertyAsString("DA_WebServerProtocol"));
		DA_WebServerMethod.setText(testElement.getPropertyAsString("DA_WebServerMethod"));
		DA_XAuthToken.setText(testElement.getPropertyAsString("DA_XAuthToken"));
		DA_PUTFile.setText(testElement.getPropertyAsString("DA_PUTFile"));
		DA_GETFile.setText(testElement.getPropertyAsString("DA_GETFile"));
		DA_HTTPSendCache.setText(testElement.getPropertyAsString("DA_HTTPSendCache"));
		DA_Abort.setSelected(testElement.getPropertyAsBoolean("DA_Abort"));
		DA_AbortSize.setText(testElement.getPropertyAsString("DA_AbortSize"));
		super.configure(testElement);
	}
}
