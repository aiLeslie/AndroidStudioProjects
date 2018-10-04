package com.example.administrator.fruitlistviewdemo;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fruit> fruitList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initFruit();
        FruitAdapter adapter = new FruitAdapter(this, R.layout.fruit_item,fruitList);

        ListView listView = (ListView)findViewById(R.id.listView) ;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fruit fruit = fruitList.get(i);
//                Toast.makeText(MainActivity.this, fruit.getComment(), Toast.LENGTH_SHORT).show();
                View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
                View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.textview_item, null);
                PopupWindow window = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
                ((TextView)contentView.findViewById(R.id.textViewDispaly)).setText(fruit.getComment());
                ((ImageView) contentView.findViewById(R.id.imageView)).setImageResource(fruit.getImageId());
                window.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
                window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
            }
        });



    }

    private void initFruit(){

        Fruit fruit = new Fruit("apple",R.drawable.apple1,"苹果（学名：Malus pumila）是水果的一种，是蔷薇科苹果亚科苹果属植物，其树为落叶乔木。苹果的果实富含矿物质和维生素，是人们经常食用的水果之一。\n" +
                "        苹果是一种低热量食物，每100克只产生60千卡热量。苹果中营养成分可溶性大，易被人体吸收，故有“活水”之称。其有利于溶解硫元素，使皮肤润滑柔嫩[1]  。据说“每天一苹果，医生远离我”。\n" +
                "        根据联合国粮农组织统计，2013年全世界的苹果产量为8082万吨，超过葡萄的7718万吨，排世界第二位（第一位是香蕉：1.067亿吨）。毫无疑问，苹果是温带水果之王。\n" +
                "        苹果营养丰富，味道甜美。[1]");
        fruitList.add(fruit);
        fruit = new Fruit("banana",R.drawable.banana_1,"香蕉（学名：Musa nana Lour.）芭蕉科芭蕉属植物，又指其果实。热带地区广泛栽培食用。香蕉味香、富含营养，终年可收获，在温带地区也很受重视。植株为大型草本，" +
                "从根状茎发出，由叶鞘下部形成高3～6公尺(10～20尺)的假杆；叶长圆形至椭圆形，有的长达3～3.5公尺(10～11.5尺)，宽65公分(26寸)，10～20枚簇生茎顶。穗状花序下垂[1]  ，由假杆顶端抽出，花多数，" +
                "淡黄色；果序弯垂，结果10～20串，约50～150个。植株结果后枯死，由根状茎长出的吸根继续繁殖，每一根株可活多年。原产亚洲东南部：台湾、海南、广东、广西等地区均有栽培。[1] ");
        fruitList.add(fruit);
        fruit = new Fruit("cherry",R.drawable.cherry1,"樱桃（学名：Cerasus pseudocerasus），是某些李属类植物的统称，包括樱桃亚属、酸樱桃亚属、桂樱亚属等。乔木，高2-6米，树皮灰白色。小枝灰褐色，嫩枝绿色，无毛或被疏柔毛。冬芽卵形，无毛。\n" +
                "果实可以作为水果食用，外表色泽鲜艳、晶莹美丽、红如玛瑙，黄如凝脂，果实富含糖、蛋白质、维生素及钙、铁、磷、钾等多种元素。\n" +
                "世界上樱桃主要分布在美国、加拿大、智利、澳洲、欧洲等地，中国主要产地有山东、安徽、江苏、浙江、河南、甘肃、陕西、四川等。[1] ");
        fruitList.add(fruit);
        fruit = new Fruit("grape",R.drawable.grape_1,"葡萄（学名：Vitis vinifera L.）为葡萄科葡萄属木质藤本植物，小枝圆柱形，有纵棱纹，无毛或被稀疏柔毛，叶卵圆形，圆锥花序密集或疏散，基部分枝发达，果实球形或椭圆形，花期4-5月，果期8-9月。\n" +
                "葡萄是世界最古老的果树树种之一，葡萄的植物化石发现于第三纪地层中，说明当时已遍布于欧、亚及格陵兰。[1]  葡萄原产亚洲西部，世界各地均有栽培，[2]  世界各地的葡萄约95%集中分布在北半球。[3] \n" +
                "葡萄为著名水果，生食或制葡萄干，并酿酒，酿酒后的酒脚可提酒食酸，根和藤药用能止呕、安胎。[2] ");
        fruitList.add(fruit);
        fruit = new Fruit("mango",R.drawable.mango_1,"芒果是杧果[1]  （中国植物志）的通俗名（拉丁学名：Mangifera indica L.），芒果是一种原产印度的漆树科常绿大乔木，叶革质，互生；花小，杂性，黄色或淡黄色，成顶生的圆锥花序。核果大，压扁，长5-10厘米，宽3-4.5厘米，成熟时黄色，味甜，果核坚硬。\n" +
                "芒果为著名热带水果之一，芒果果实含有糖、蛋白质、粗纤维，芒果所含有的维生素A的前体胡萝卜素成分特别高，是所有水果中少见的。其次维生素C含量也不低。矿物质、蛋白质、脂肪、糖类等，也是其主要营养成分。可制果汁、果酱、罐头、腌渍、酸辣泡菜及芒果奶粉、蜜饯等。");
        fruitList.add(fruit);
        fruit = new Fruit("orange",R.drawable.orange_1,"橙子（学名：Citrus sinensis 英语：orange），是芸香科柑橘属植物橙树的果实，亦称为黄果、柑子、金环、柳丁。橙子是一种柑果，有很高的食用，药用价值。\n" +
                "橙子起源于东南亚。橙树属小乔木。果实可以剥皮鲜食其果肉，果肉可以用作其他食物的调料或附加物。");
        fruitList.add(fruit);
        fruit = new Fruit("pear",R.drawable.pear_1,"梨，通常品种是一种落叶乔木或灌木，极少数品种为常绿，属于被子植物门双子叶植物纲蔷薇科苹果亚科。叶片多呈卵形，大小因品种不同而各异。花为白色，或略带黄色、粉红色，有五瓣。果实形状有圆形的，也有基部较细尾部较粗的，即俗称的“梨形”；不同品种的果皮颜色大相径庭，有黄色、绿色、黄中带绿、绿中带黄、黄褐色、绿褐色、红褐色、褐色，个别品种亦有紫红色；野生梨的果径较小，在1到4厘米之间，而人工培植的品种果径可达8厘米，长度可达18厘米。\n" +
                "梨的果实通常用来食用，不仅味美汁多，甜中带酸，而且营养丰富，含有多种维生素和纤维素，不同种类的梨味道和质感都完全不同。梨既可生食，也可蒸煮后食用。在医疗功效上，梨可以通便秘，利消化，对心血管也有好处。在民间，梨还有一种疗效，把梨去核，放入冰糖，蒸煮过后食用还可以止咳；除了作为水果食用以外，梨还可以作观赏之用。");
        fruitList.add(fruit);
        fruit = new Fruit("pineapple",R.drawable.pineapple_1,"菠萝（学名：Ananas comosus），是热带水果之一。福建和台湾地区称之为旺梨或者旺来（ông-lâi），新马一带称为黄梨，大陆及香港称作菠萝。有70多个品种，岭南四大名果之一。\n" +
                "菠萝原产于南美洲巴西、巴拉圭的亚马逊河流域一带，16世纪从巴西传入中国。 现在已经流传到整个热带地区。其可食部分主要由肉质增大之花序轴、螺旋状排列于外周的花组成，花通常不结实，宿存的花被裂片围成一空腔，腔内藏有萎缩的雄蕊和花柱。叶的纤维甚坚韧，可供织物、制绳、结网和造纸。[1] \n" +
                "凤梨与菠萝在生物学上是同一种水果。[2]  市场上，凤梨与菠萝为不同品种水果：菠萝削皮后有“内刺”需要剔除；而凤梨消掉外皮后没有“内刺”，不需要刀到划出一道道沟。[3] ");
        fruitList.add(fruit);
        fruit = new Fruit("strawberry",R.drawable.strawberry_1,"草莓（学名：Fragaria × ananassa Duch.），多年生草本植物。高10-40厘米，茎低于叶或近相等，密被开展黄色柔毛。叶三出，小叶具短柄，质地较厚，倒卵形或菱形，上面深绿色，几无毛，下面淡白绿色，疏生毛，沿脉较密；叶柄密被开展黄色柔毛。聚伞花序，花序下面具一短柄的小叶；花两性；萼片卵形，比副萼片稍长；花瓣白色，近圆形或倒卵椭圆形。聚合果大，宿存萼片直立，紧贴于果实；瘦果尖卵形，光滑。花期4-5月，果期6-7月[1]  。\n" +
                "原产南美，中国各地及欧洲等地广为栽培。草莓营养价值高，含有多种营养物质 ，且有保健功效。");
        fruitList.add(fruit);
        fruit = new Fruit("watermelon",R.drawable.watermelon_1,"西瓜（学名：Citrullus lanatus (Thunb.) Matsum. et Nakai）一年生蔓生藤本；茎、枝粗壮，具明显的棱。卷须较粗壮，具短柔毛，叶柄粗，密被柔毛；叶片纸质，轮廓三角状卵形，带白绿色，两面具短硬毛，叶片基部心形。雌雄同株。雌、雄花均单生于叶腋。雄花花梗长3-4厘米，密被黄褐色长柔毛；花萼筒宽钟形；花冠淡黄色；雄蕊近离生，花丝短，药室折曲。雌花：花萼和花冠与雄花同；子房卵形，柱头肾形。果实大型，近于球形或椭圆形，肉质，多汁，果皮光滑，色泽及纹饰各式。种子多数，卵形，黑色、红色，两面平滑，基部钝圆，通常边缘稍拱起，花果期夏季。\n" +
                "中国各地栽培，品种甚多，外果皮、果肉及种子形式多样，以新疆、甘肃兰州、山东德州、江苏东台等地最为有名。其原种可能来自非洲，久已广泛栽培于世界热带到温带，金、元时始传入中国。\n" +
                "西瓜为夏季之水果，果肉味甜，能降温去暑；种子含油，可作消遣食品；果皮药用，有清热、利尿、降血压之效。");
        fruitList.add(fruit);




    }
}
