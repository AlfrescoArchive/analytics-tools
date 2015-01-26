package org.alfresco.analytics.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.alfresco.analytics.core.Calculator;
import org.alfresco.analytics.core.CommonsCalculator;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.filefolder.RandomFileInfo;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.QName;
import org.springframework.stereotype.Component;

/**
 * Gives random file info.
 *
 * @author Gethin James
 */

@Component
public class RandomEnricherHelper
{   
    public static String RAND_TXT = "RND";
    private Calculator calc = new CommonsCalculator();
    static int filenamesUpper;
    static int fileEndingsUpper;
    static int mimetypesUpper;

    FileInfo getFileInfo(String nodeId) 
    {
        String nodeEnd = nodeId.substring(nodeId.length()-4);
        ContentData contentData = new ContentData("abc://xxx"+nodeEnd, mimetypes[calc.random(0,mimetypesUpper)], calc.random(1,1000000)*1000, "UTF-8", Locale.ENGLISH);
        Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
        properties.put(ContentModel.PROP_NAME, filenames[calc.random(0,filenamesUpper)]+fileEndings[calc.random(0,fileEndingsUpper)]);
        properties.put(ContentModel.PROP_CONTENT, contentData);
        return new RandomFileInfo(new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, nodeId), ContentModel.PROP_CONTENT, false, false, properties);
    }
    
    static final String[] filenames = {"Nisi Cum Company" , "Libero Morbi Institute" , "Phasellus At PC" , "Sit Ltd" , 
        "Morbi Vehicula Pellentesque Institute" , "Libero Est Congue Ltd" , "Dui Corporation" , "Nam Ligula Foundation" , "Diam Industries" , 
        "Aenean Gravida Nunc Company" , "Quis Pede Company" , "Vel Foundation" , "Elementum Dui Quis Institute" , "Neque Vitae Corp." , 
        "Magnis Dis Parturient LLP" , "Eu Odio Tristique LLC" , "Faucibus PC" , "Placerat Velit LLP" , "Aliquam Limited" , "Sed Sem Corporation" , 
        "Sapien Industries" , "Volutpat Ornare Facilisis Industries" , "Dui Fusce Aliquam LLC" , "Nunc Mauris Sapien Incorporated" , 
        "Tincidunt Consulting" , "Turpis Institute" , "Eu Odio LLC" , "Quisque Foundation" , "Phasellus Fermentum Convallis Incorporated" , 
        "Ac Mattis Semper Consulting" , "Dui Cum Sociis PC" , "Velit Egestas Lacinia LLC" , "Sed Ltd" , "Cras Interdum LLC" , 
        "Cursus Luctus Ipsum Incorporated" , "Auctor PC" , "Vulputate Lacus Cras Company" , "Ornare Inc." , "Id Sapien Associates" , 
        "Sed Molestie Sed Limited" , "Et Netus Inc." , "Accumsan Foundation" , "Semper Ltd" , "Sit PC" , "Aenean Foundation" , 
        "Vulputate Velit Eu Foundation" , "Enim Commodo Hendrerit Inc." , "Eu Erat Semper Associates" , "Nisi Mauris LLP" , "Vestibulum Associates" , 
        "Aliquam Nisl Nulla LLP" , "Ligula LLP" , "Felis Orci Adipiscing Limited" , "Sed Consulting" , "Nec LLC" , "Ac Mi Eleifend Inc." , 
        "Velit Eget LLC" , "Fringilla Institute" , "Ornare Libero Institute" , "Non Sapien Molestie Consulting" , "Mi PC" , "Nibh Lacinia Limited" , 
        "Vitae Nibh Consulting" , "Cum Sociis Natoque Foundation" , "Vel Turpis Ltd" , "Diam Vel PC" , "Mauris Ut Quam Corporation" , 
        "Pellentesque Corporation" , "Magna Company" , "Nibh Sit Inc." , "Et Netus Limited" , "Enim Nunc PC" , "Suspendisse Institute" , 
        "Dui Consulting" , "At Velit PC" , "Venenatis Lacus Etiam Foundation" , "Dolor Egestas Consulting" , "Enim Company" , "Ullamcorper Duis Corp." , 
        "Vulputate Lacus Cras Inc." , "Neque Sed Eget Industries" , "Odio Auctor Company" , "Lacus Pede Incorporated" , "Non Magna PC" , 
        "Libero Proin Sed PC" , "Molestie Arcu Sed Corporation" , "Egestas A Dui Incorporated" , "Libero Nec Ligula Corp." , "Arcu Ac Foundation" , 
        "Volutpat Nulla Consulting" , "Faucibus Limited" , "Urna Limited" , "Erat Volutpat Limited" , "Nullam Limited" , "Pede Malesuada Vel Inc." , 
        "Convallis Ante Lectus Ltd" , "Erat Foundation" , "Mi Enim Condimentum LLC" , "Phasellus PC" , "Amet Risus Donec Associates",
        "Eleifend Vitae Incorporated", "Vulputate PC", "Donec Elementum Company", "Nec LLC", "Sed Nec Company", "Arcu Ltd", "Eget Industries", 
        "Nisl Arcu Iaculis Inc.", "Ante Dictum Mi Company", "Maecenas Ornare Egestas Limited", "Nulla Institute", "Ipsum Phasellus Vitae LLC", 
        "Egestas Consulting", "Nec Orci Donec PC", "Viverra Donec Tempus PC", "Amet Consectetuer Adipiscing Limited", "Dictum Proin Eget LLP", 
        "Ullamcorper Inc.", "Torquent Per Conubia Associates", "Dis Parturient Consulting", "Metus Inc.", "Enim Foundation", "Morbi LLC", 
        "Parturient Montes Nascetur Limited", "Semper Pretium Neque LLP", "Vitae Institute", "Pede Sagittis Augue Incorporated", "Ornare Elit Elit Inc.", 
        "Diam At Pretium Foundation", "Sit Amet Ltd", "Scelerisque Dui Suspendisse Limited", "Nisi Mauris Nulla Consulting", "In LLC", 
        "Suspendisse Ac Institute", "Ac Limited", "Odio LLP", "Massa Corp.", "Habitant Morbi Industries", "Facilisis Suspendisse Commodo Institute", 
        "Enim Mauris Institute", "Lobortis Corp.", "Et Magnis Dis Ltd", "Eu Augue Porttitor LLC", "Scelerisque PC", "Quam Inc.", 
        "Risus Donec Corporation", "Velit Associates", "Elementum Lorem Ut Industries", "Erat Vitae LLC", "Penatibus Limited", "Ultrices A Auctor Corp.", 
        "Non Inc.", "Sit Amet LLC", "Vivamus Euismod Urna Associates", "Quis PC", "Nisl Quisque Institute", "Urna Justo Corporation", "Aliquet Proin Incorporated", "Phasellus In Institute", "Vel Venenatis Vel Corp.", "Non Egestas A LLP", "Euismod Urna Company", "Non Cursus Non Company", "Vel Incorporated", "Leo Vivamus Nibh Associates", "Tellus Eu Augue Company", "Mattis Corporation", "Elit LLP", "Vel Sapien Imperdiet Associates", "Sed LLC", "Orci Adipiscing Industries", "Enim Nec LLC", "Massa Lobortis Ultrices Foundation", "Ut Dolor Dapibus Consulting", "Vulputate Ullamcorper Incorporated", "Nam Ltd", "Erat Inc.", "Semper Company", "Neque In Incorporated", "Nisi Magna Sed Incorporated", "Magna Ut Ltd", "Nam Inc.", "Magna Lorem Foundation", "Eu Dui Cum Incorporated", "Praesent Industries", "Nisi Associates", "Vel Sapien Industries", "Et Libero Proin Institute", "Sed Eget Associates", "Aliquet Consulting", "Lectus Ante Ltd", "Sed Nec Corp.", "Et Rutrum Limited", "Sed Eu Nibh Limited", "Eu Eros Nam Associates", "Fames Incorporated", "Tincidunt Nunc Corp.", "Ac Mattis Limited", "Donec LLP", "Nunc Inc.", "Vitae Purus Gravida Consulting", "Cursus Corp.", "Ante Maecenas Mi Corporation", "Libero Nec Ligula Foundation", "Cubilia Curae; Donec Limited", "Risus Morbi LLC", "Enim Sit Amet Incorporated", "Accumsan Limited", "Imperdiet Ornare Foundation", "Non Consulting", "Lorem Eu Industries", "Ultrices Consulting", "Sed Eu LLP", "Vulputate Eu PC", "Sodales Industries", "Nunc In At Associates", "Dapibus Ligula Consulting", "Integer Sem Company", "Mi Tempor Lorem Incorporated", "Purus Gravida Sagittis Corporation", "Volutpat Nunc Associates", "Aliquam Ornare Consulting", "Faucibus LLC", "Erat In Company", "Elit Foundation", "Tortor Nunc Corp.", "Eget Company", "Dolor Donec Fringilla Institute", "Etiam Company", "Dictum Ultricies Corporation", "Parturient Incorporated", "Sem Pellentesque Ut Industries", "Adipiscing LLP", "Proin Eget LLC", "Cras Lorem Lorem PC", "Et Ultrices Company", "Parturient Corporation", "Lorem Lorem Limited", "Volutpat Nunc Corporation", "Sem Consequat Nec Inc.", "Felis Inc.", "Nascetur Limited", "In Dolor Corporation", "Lobortis Corporation", "Ligula Elit LLP", "Ac Libero Corp.", "Cum Sociis Natoque Foundation", "Quisque Tincidunt Pede PC", "Libero Corp.", "Sapien Nunc Pulvinar Incorporated", "Congue PC", "Mi Pede LLP", "Elit Fermentum Risus Foundation", "Neque Tellus Consulting", "Integer Industries", "Convallis Erat Corp.", "Curabitur Ut Odio Associates", "Nec Metus Corporation", "Sed Diam Company", "Commodo Tincidunt Nibh Corp.", "Primis In Ltd", "Dictum Eu Corporation", "Et Arcu Imperdiet Associates", "Non Sollicitudin A LLP", "Justo Faucibus Lectus Industries", "Lectus Pede Ultrices Consulting", "Commodo Auctor Consulting", "Ac Sem LLC", "Pede Nec Ante Inc.", "Fermentum Vel Mauris Industries", "Vitae Corporation", "Non Enim Foundation", "Purus Corp.", "Sed Nunc Est Company", "Tincidunt Dui Augue Consulting", "Ante Iaculis Ltd", "Neque Institute", "Interdum Sed Auctor Consulting", "Mauris Suspendisse PC", "Erat Corp.", "Libero Lacus Institute", "Morbi Neque Tellus Corp.", "Arcu Et Pede Consulting", "Nam Porttitor Scelerisque Consulting", "Bibendum Industries", "Et Institute", "Ac Corp.", "Rutrum Non Hendrerit Associates", "Donec Luctus PC", "Sed Company", "Ultrices LLC", "Tempus Institute", "Phasellus Libero Mauris LLP", "Aliquet Phasellus PC", "Quisque Libero Lacus Industries", "Consequat Auctor Nunc Inc.", "Urna Convallis LLP", "Rhoncus Id Mollis Industries", "Augue Id Ante Associates", "Etiam Laoreet LLP", "Arcu Vivamus Corporation" , "Vel Nisl Foundation" , "Faucibus Orci Luctus Corp." , "Iaculis Odio Institute" , "Rutrum Non Hendrerit LLP" , "Ut Nisi A Ltd" , "In Aliquet Lobortis Inc." , "Tortor At Inc." , "Quam Dignissim Pharetra Inc." , "Non Leo Vivamus LLP" , "Phasellus At Company" , "Sapien Incorporated" , "Ante Lectus Convallis Incorporated" , "Risus PC" , "A Arcu Institute" , "Phasellus Elit Pede Associates" , "Rutrum Eu Ultrices Company" , "Tempor Lorem Eget Ltd" , "Gravida Company" , "In Lobortis Tellus Limited" , "Eget Metus LLP" , "A Ultricies Foundation" , "Fringilla Company" , "Praesent Eu Corporation" , "Euismod Urna Institute" , "Adipiscing Lacus Incorporated" , "Mattis Ornare Lectus Incorporated" , "Eu Arcu Consulting" , "Sed Et Libero Corp." , "Augue Porttitor Corp." , "Pede Nec Ante Ltd" , "Eros Consulting" , "Phasellus Consulting" , "Convallis Limited" , "Dignissim Corporation" , "Quam Elementum Institute" , "Laoreet Ipsum Company" , "Neque Non Quam Foundation" , "Et Rutrum Eu LLC" , "Lorem Ut Aliquam Incorporated" , "Bibendum PC" , "Nisl Nulla Industries" , "Mus Industries" , "Metus Facilisis Inc." , "Amet Metus Ltd" , "Orci Institute" , "Eu Dui Cum LLC" , "Leo Morbi Limited" , "Adipiscing Non LLC" , "Massa Suspendisse Incorporated" , "Suspendisse Eleifend LLP" , "Neque LLC" , "Risus Quisque Industries" , "Vitae Purus Corporation" , "Nec Limited" , "Ac Facilisis Industries" , "Ac Inc." , "Tristique Corp." , "Donec Corporation" , "Et LLP" , "In Faucibus Company" , "Montes Nascetur Ridiculus Associates" , "Nullam Ut Nisi Incorporated" , "Mattis PC" , "Rutrum Urna Corp." , "Nullam Ut Foundation" , "Auctor Quis Tristique Foundation" , "Id Sapien Cras Limited" , "Proin Dolor PC" , "Tincidunt Ltd" , "In Faucibus Company" , "Eu Ligula Ltd" , "Nulla Company" , "Etiam Vestibulum Industries" , "Curabitur Sed Tortor Corp." , "Venenatis Vel LLP" , "Pede Nonummy Associates" , "Et Foundation" , "Libero Inc." , "Tellus Phasellus Company" , "Mauris Morbi Non LLC" , "Risus Industries" , "Non Lacinia At Corp." , "Sit Corporation" , "Quam Ltd" , "Vitae Dolor LLP" , "Ornare Elit Corp." , "Velit Justo Company" , "Aliquam Rutrum Lorem Associates" , "Morbi Metus Vivamus Foundation" , "Risus Limited" , "Fringilla Mi PC" , "Nec Tempus Scelerisque Inc." , "Mattis Ornare Lectus Incorporated" , "Euismod Et Corporation" , "Suspendisse Aliquet Molestie Corporation" , "Elit Sed Industries" , "Ultrices Duis Volutpat Associates" , "Porta Elit PC" , "Et Tristique Limited"};
    
    static final String[] fileEndings  = {" Presentation", " Proposal", " Report", " Overview", " Results", " List", " Document", " Draft", " Case Study"
                                        , " White Paper", " Demo", " Samples", " Materials", " Products", " Tasks" , " Plan" 
                                        , " Notes", " Agenda", " Figures", " Details" , " For Review", " Approved", " For Release"};
                
    static final String[] mimetypes = {MimetypeMap.MIMETYPE_EXCEL, MimetypeMap.MIMETYPE_HTML, MimetypeMap.MIMETYPE_PDF, MimetypeMap.MIMETYPE_PPT, 
        MimetypeMap.MIMETYPE_MP3,MimetypeMap.MIMETYPE_ATOM, MimetypeMap.MIMETYPE_IMAGE_GIF, MimetypeMap.MIMETYPE_OPENDOCUMENT_TEXT_WEB, 
        MimetypeMap.MIMETYPE_ZIP, MimetypeMap.MIMETYPE_OPENXML_PRESENTATION,  MimetypeMap.MIMETYPE_TEXT_PLAIN, MimetypeMap.MIMETYPE_IMAGE_JPEG,
        MimetypeMap.MIMETYPE_OPENXML_WORDPROCESSING,  MimetypeMap.MIMETYPE_OPENDOCUMENT_SPREADSHEET,  MimetypeMap.MIMETYPE_XML,
        MimetypeMap.MIMETYPE_OPENXML_WORD_TEMPLATE, MimetypeMap.MIMETYPE_JSON, MimetypeMap.MIMETYPE_WORD, MimetypeMap.MIMETYPE_TEXT_CSV};
    static
    {
        filenamesUpper = filenames.length-1;
        fileEndingsUpper = fileEndings.length-1;
        mimetypesUpper = mimetypes.length-1;
    }
    
}
