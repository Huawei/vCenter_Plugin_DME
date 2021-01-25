import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 2, name: 'Helium', weight: 4.0026, symbol: 'He' },
  { position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li' },
  { position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be' },
  { position: 5, name: 'Boron', weight: 10.811, symbol: 'B' },
  { position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C' },
  { position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N' },
  { position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O' },
  { position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F' },
  { position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne' },
];

const MESSAGES = [
  {
    img: 'assets/images/avatars/avatar-1.jpg',
    subject: 'Hydrogen',
    content: `Cras sit amet nibh libero, in gravida nulla.
     Nulla vel metus scelerisque ante sollicitudin commodo.`,
  },
  {
    img: 'assets/images/avatars/avatar-2.jpg',
    subject: 'Helium',
    content: `Cras sit amet nibh libero, in gravida nulla.
     Nulla vel metus scelerisque ante sollicitudin commodo.`,
  },
  {
    img: 'assets/images/avatars/avatar-3.jpg',
    subject: 'Lithium',
    content: `Cras sit amet nibh libero, in gravida nulla.
     Nulla vel metus scelerisque ante sollicitudin commodo.`,
  },
  {
    img: 'assets/images/avatars/avatar-4.jpg',
    subject: 'Beryllium',
    content: `Cras sit amet nibh libero, in gravida nulla.
     Nulla vel metus scelerisque ante sollicitudin commodo.`,
  },
  {
    img: 'assets/images/avatars/avatar-6.jpg',
    subject: 'Boron',
    content: `Cras sit amet nibh libero, in gravida nulla.
     Nulla vel metus scelerisque ante sollicitudin commodo.`,
  },
];

@Injectable()
export class DashboardService {
  stats = [
    {
      title: 'Total Sales',
      amount: '180,200',
      progress: {
        value: 50,
      },
      color: 'bg-indigo-500',
    },
    {
      title: 'Revenue',
      amount: '70,205',
      progress: {
        value: 70,
      },
      color: 'bg-blue-500',
    },
    {
      title: 'Traffic',
      amount: '1,291,922',
      progress: {
        value: 80,
      },
      color: 'bg-green-500',
    },
    {
      title: 'New User',
      amount: '1,922',
      progress: {
        value: 40,
      },
      color: 'bg-teal-500',
    },
  ];

  charts = [
    {
      tooltip: {
        trigger: 'item',
        formatter: ' {b}: {c} ({d}%)'
      },
      title: {
        text: '123',
        textAlign: 'center',
        padding: 0,
        textVerticalAlign: 'middle',
        textStyle: {
          fontSize: 22,
          color: '#63B3F7'
        },
        subtextStyle: {
          fontSize: 12,
          color: '#c2c6dc',
          align: 'center'
        },
        left: '50%',
        top: '50%',
        //subtext: '234'
      },

      series: [
        {
          name: '',
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],

          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: false,
              fontSize: '30',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            {value: 335, name: '直接访问'},
            {value: 310, name: '邮件营销'},
            {value: 234, name: '联盟广告'},
            {value: 135, name: '视频广告'},
            {value: 1548, name: '搜索引擎'}
          ]
        }
      ],
      color: ['#FF0000', '#FF9538', '#63B3F7']
    },
    {
      tooltip: {
        trigger: 'item',
        formatter: ' {b}: {c} ({d}%)'
      },
      title: {
        text: '123',
        textAlign: 'center',
        padding: 0,
        textVerticalAlign: 'middle',
        textStyle: {
          fontSize: 22,
          color: '#63B3F7'
        },
        subtextStyle: {
          fontSize: 12,
          color: '#c2c6dc',
          align: 'center'
        },
        left: '50%',
        top: '50%',
        //subtext: '234'
      },

      series: [
        {
          name: '',
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],

          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: false,
              fontSize: '30',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            {value: 335, name: '直接访问'},
            {value: 310, name: '邮件营销'},
            {value: 234, name: '联盟广告'},
            {value: 135, name: '视频广告'},
            {value: 1548, name: '搜索引擎'}
          ]
        }
      ],
      color: ['#FF0000', '#FF9538', '#63B3F7']
    },
    {
      tooltip: {
        trigger: 'item',
        formatter: ' {b}: {c} ({d}%)'
      },
      title: {
        text: '123',
        textAlign: 'center',
        padding: 0,
        textVerticalAlign: 'middle',
        textStyle: {
          fontSize: 18,
          color: '#63B3F7'
        },
        subtextStyle: {
          fontSize: 12,
          color: '#c2c6dc',
          align: 'center'
        },
        left: '50%',
        top: '50%',
        //subtext: '234'
      },

      series: [
        {
          name: '',
          type: 'pie',
          radius: ['60%', '70%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: false,
              fontSize: '20',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            {value: 335, name: '直接访问'},
            {value: 310, name: '邮件营销'}
          ]
        }
      ],
      color: ['hsl(198, 100%, 32%)', 'hsl(198, 0%, 80%)']
    },
    {
      xAxis: {
        show: false,
        type: 'category'
      },
      yAxis: {
        show: false,
        type: 'value'
      },
      series: [{
        data: [120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130],
        type: 'bar',
        barCategoryGap: 0
      }],
      color: ['hsl(198, 100%, 32%)']
    }
  ];

  storageNumOption = {
    tooltip: {
      trigger: 'item',
      formatter: ' {b}: {c} ({d}%)'
    },
    title: {},
    series: [
      {
        name: '',
        type: 'pie',
        radius: ['60%', '80%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: false,
            fontSize: '20',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          {value: 8, name: 'normal'},
          {value: 22, name: 'abnormal'},
        ]
      }
    ],
    color: ['#92CF5A', '#CA2200']
  };
  storageCapacityOption = {
    tooltip: {
      trigger: 'item',
      formatter: ' {b}: {c} ({d}%)',
      position: 'right'
    },
    title: {
      text: '0.0 %',
      textAlign: 'center',
      padding: 0,
      textVerticalAlign: 'middle',
      textStyle: {
        fontSize: 18,
        color: '#63B3F7'
      },
      subtextStyle: {
        fontSize: 12,
        color: '#c2c6dc',
        align: 'center'
      },
      left: '50%',
      top: '50%',
      //subtext: '234'
    },
    series: [
      {
        name: '',
        type: 'pie',
        radius: ['60%', '80%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: false,
            fontSize: '20',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          {value: 8, name: 'normal'},
          {value: 22, name: 'abnormal'},
        ]
      }
    ],
    color: ['hsl(198, 100%, 32%)', 'hsl(198, 0%, 80%)']
  };

  constructor(private http: HttpClient) {}

  getData() {
    return ELEMENT_DATA;
  }

  getMessages() {
    return MESSAGES;
  }

  getCharts() {
    return this.charts;
  }

  getStats() {
    return this.stats;
  }
}
